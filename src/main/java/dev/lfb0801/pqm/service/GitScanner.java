package dev.lfb0801.pqm.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static dev.lfb0801.pqm.util.Unchecked.uncheck;
import static java.util.stream.StreamSupport.stream;

@Service
public class GitScanner {

    public final Git git;

    public GitScanner(Git git) {
        this.git = git;
    }

    public Set<String> getFilesInRepository() throws IOException {
        Repository repo = git.getRepository();

        ObjectId headId = repo.resolve(Constants.HEAD);
        try (RevWalk revWalk = new RevWalk(git.getRepository())) {
            RevCommit commit = revWalk.parseCommit(headId);
            return getFilesInCommit(commit.getTree()
                                          .getId());
        }
    }

    public Map.Entry<String, Long> countCommits(String file) throws GitAPIException, IOException {
        return Map.entry(file, stream(git.log()
                                         .all()
                                         .call()
                                         .spliterator(), true
                         ).map(uncheck((RevCommit c) -> getFilesInCommit(c.getTree()
                                                                          .getId())))
                          .filter(s -> s.contains(file))
                          .count()
        );
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
