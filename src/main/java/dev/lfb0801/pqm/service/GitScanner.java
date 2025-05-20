package dev.lfb0801.pqm.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.springframework.stereotype.Service;

@Service
public class GitScanner {

    public final Git git;

    public GitScanner(Git git) {
        this.git = git;
    }

    public Set<String> getFilesInRepository() throws IOException {
        var repo = git.getRepository();

        var headId = repo.resolve(Constants.HEAD);
        try (var revWalk = new RevWalk(git.getRepository())) {
            return getFilesInCommit(revWalk.parseCommit(headId)
                                        .getTree()
                                        .getId());
        }
    }

    public Map.Entry<String, Long> countCommits(String file) throws GitAPIException, IOException {
        long count = 0;

        var repo = git.getRepository();
        var commits = git.log()
            .add(repo.resolve("HEAD"))
            .call();

        try (RevWalk revWalk = new RevWalk(repo); DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {

            diffFormatter.setRepository(repo);
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);

            for (RevCommit commit : commits) {
                AbstractTreeIterator oldTreeIter;
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                newTreeIter.reset(repo.newObjectReader(), commit.getTree());

                if (commit.getParentCount() > 0) {
                    RevCommit parent = revWalk.parseCommit(commit.getParent(0)
                                                               .getId());
                    CanonicalTreeParser parentTreeIter = new CanonicalTreeParser();
                    parentTreeIter.reset(repo.newObjectReader(), parent.getTree());
                    oldTreeIter = parentTreeIter;
                } else {
                    oldTreeIter = new EmptyTreeIterator();
                }

                List<DiffEntry> diffs = diffFormatter.scan(oldTreeIter, newTreeIter);

                for (DiffEntry diff : diffs) {
                    if (file.equals(diff.getNewPath()) || file.equals(diff.getOldPath())) {
                        count++;
                        break;
                    }
                }
            }
        }

        return Map.entry(file, count);
    }

    private Set<String> getFilesInCommit(ObjectId id) throws IOException {
        Set<String> committed = new HashSet<>();
        try (TreeWalk tw = new TreeWalk(git.getRepository())) {
            tw.addTree(id);
            tw.setRecursive(true);
            while (tw.next()) {
                committed.add(tw.getPathString());
            }
            return committed;
        }
    }
}
