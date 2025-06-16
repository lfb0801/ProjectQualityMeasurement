package dev.lfb0801.pqm.service;

import dev.lfb0801.pqm.domain.Aggregate;
import org.cthing.locc4j.Counts;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.lfb0801.pqm.domain.Aggregate.build;

@Service
public class AggregateService {

	private final LOCScanner locScanner;
	private final GitScanner gitScanner;
	private final Git git;

	public AggregateService(LOCScanner locScanner, GitScanner gitScanner, Git git) {
		this.locScanner = locScanner;
		this.gitScanner = gitScanner;
		this.git = git;
	}

	public Set<Aggregate> aggregate() throws IOException {
		Set<String> files = gitScanner.getFilesInRepository();
		var sources = locScanner.scanSources();
		var tests = locScanner.scanTests();

		return files.stream()
				.map(file -> build(aggregate -> aggregate //
						.file(file)
						.locSrc(getLinesOfCodeFor(sources, file))
						.locTest(getLinesOfCodeFor(tests, file))
						.commits(getCommits(file))))
				.collect(Collectors.toSet());
	}

	private Set<String> getCommits(String file) {
		try {
			return gitScanner.getCommitsContainingFile(file);
		} catch (GitAPIException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int getLinesOfCodeFor(Map<Path, Counts> scanResult, String file) {
		return Optional.ofNullable(scanResult.getOrDefault(//
						new File(git.getRepository()
								.getWorkTree(), file
						).toPath(), null
				))
				.map(Counts::getCodeLines)
				.orElse(0);
	}
}
