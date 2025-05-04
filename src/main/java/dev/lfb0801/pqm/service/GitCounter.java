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
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

@Service
public class GitCounter {

    public final Git git;

    public GitCounter(Git git) {
        this.git = git;
    }

    public Set<String> getEveryCommittedFile() throws IOException {
        Repository repo = git.getRepository();

        ObjectId headId = repo.resolve(Constants.HEAD);
        Set<String> committed = new HashSet<>();
        try (RevWalk revWalk = new RevWalk(repo)) {
            RevCommit commit = revWalk.parseCommit(headId);
            ObjectId treeId = commit.getTree()
                                    .getId();
            try (TreeWalk tw = new TreeWalk(repo)) {
                tw.addTree(treeId);
                tw.setRecursive(true);
                while (tw.next()) {
                    committed.add(tw.getPathString());
                }
            }
        }
        return committed;
    }

    public Map<Path, Integer> countCommits(Path path) {
        try {
            List<RevCommit> list = StreamSupport.stream(git.log()
                                                           .addPath(path.toString())
                                                           .call()
                                                           .spliterator(), false)
                                                .toList();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        return Map.of();
    }
}
