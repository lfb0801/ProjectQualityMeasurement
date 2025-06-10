package dev.lfb0801.pqm.service;

import org.cthing.locc4j.CountUtils;
import org.cthing.locc4j.CountingTreeWalker;
import org.cthing.locc4j.Counts;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Service
public class LOCScanner {

	private final Path path;

	public LOCScanner(Git git) {
		this.path = git.getRepository()
				.getWorkTree()
				.toPath();
	}

	public Map<Path, Counts> scanSources() throws IOException {
		return matchPattern("**/src/main/**");
	}

	public Map<Path, Counts> scanTests() throws IOException {
		return matchPattern("**/src/test/**");
	}

	private Map<Path, Counts> matchPattern(String pattern) throws IOException {
		final var walker = //
				new CountingTreeWalker(path, pattern).respectGitignore(true)
						.countDocStrings(false);

		return CountUtils.byFile(walker.count());
	}
}
