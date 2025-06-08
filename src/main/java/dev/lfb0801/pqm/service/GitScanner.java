package dev.lfb0801.pqm.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
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

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.lfb0801.pqm.Unchecked.Errors.suppress;
import static java.util.stream.StreamSupport.stream;

@Service
public class GitScanner {

	private final Git git;

	public GitScanner(Git git) {
		this.git = git;
	}

	public Set<String> getFilesInRepository() throws IOException {
		var repo = git.getRepository();

		var headId = repo.resolve(Constants.HEAD);
		try (var revWalk = new RevWalk(git.getRepository())) {
			return listFilesInCommit(revWalk.parseCommit(headId)
					.getTree()
					.getId());
		}
	}

	public Set<String> getCommitsContainingFile(String file) throws GitAPIException, IOException {
		final var repo = git.getRepository();
		final var commits = git.log()
				.add(repo.resolve("HEAD"))
				.call();

		try (var revWalk = new RevWalk(repo); var diffFormatter = diffFormatter(repo)) {

			return stream(commits.spliterator(), false) //
					.flatMap(suppress().wrap(commit -> {
						final var newTreeIter = currentTreeIterator(commit, repo);
						final var oldTreeIter = previousTreeIterator(commit, revWalk, repo);

						return diffFormatter.scan(oldTreeIter, newTreeIter)
								.stream()
								.filter(diff -> diff.getNewPath()
										                .equals(file) || diff.getOldPath()
										                .equals(file))
								.map($ -> commit.name());
					}))
					.collect(Collectors.toSet());
		}
	}

	private DiffFormatter diffFormatter(Repository repo) {
		var diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		diffFormatter.setRepository(repo);
		diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		diffFormatter.setDetectRenames(true);
		return diffFormatter;
	}

	private AbstractTreeIterator currentTreeIterator(RevCommit commit, Repository repo) throws IOException {
		var newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(repo.newObjectReader(), commit.getTree());
		return newTreeIter;
	}

	private AbstractTreeIterator previousTreeIterator(RevCommit commit, RevWalk revWalk, Repository repo) throws IOException {
		if (commit.getParentCount() > 0) {
			var parent = revWalk.parseCommit(commit.getParent(0)
					.getId());
			var parentTreeIter = new CanonicalTreeParser();
			parentTreeIter.reset(repo.newObjectReader(), parent.getTree());
			return parentTreeIter;
		} else {
			return new EmptyTreeIterator();
		}
	}

	private Set<String> listFilesInCommit(ObjectId id) throws IOException {
		try (var tw = new TreeWalk(git.getRepository())) {
			tw.addTree(id);
			tw.setRecursive(true);

			return Optional.of(tw)
					.stream()
					.takeWhile(treeWalk -> {
						try {
							return treeWalk.next();
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					})
					.map(TreeWalk::getPathString)
					.collect(Collectors.toSet());
		}
	}
}
