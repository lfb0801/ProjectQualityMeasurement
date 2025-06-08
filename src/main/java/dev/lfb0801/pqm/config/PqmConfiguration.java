package dev.lfb0801.pqm.config;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static dev.lfb0801.pqm.Constants.TARGET_PATH;
import static java.util.Comparator.reverseOrder;

@Configuration
@ComponentScan(basePackages = "dev.lfb0801.pqm")
public class PqmConfiguration {

	private final String targetPath;

	public PqmConfiguration(@Value("${quality.target.path}") String targetPath) {
		this.targetPath = targetPath;
	}

	@Bean
	public Repository repo() throws IOException {
		Path foo = Path.of(TARGET_PATH.value);
		if (Files.exists(foo)) {
			Files.walk(foo)
					.sorted(reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
		Files.createDirectory(foo);

		return new RepositoryBuilder().setWorkTree(new File(foo.toString()))
				.setup()
				.build();
	}

	@Bean
	public Git git(Repository repo) {
		Git git = new Git(repo);
		try {
			Git.cloneRepository()
					.setURI(targetPath)
					.setDirectory(repo.getWorkTree())
					.call();
		} catch (GitAPIException e) {
			throw new IllegalArgumentException("Repository should already exist", e);
		}
		return git;
	}
}
