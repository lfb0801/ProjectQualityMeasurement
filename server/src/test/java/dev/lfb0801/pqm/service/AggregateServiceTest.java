package dev.lfb0801.pqm.service;

import dev.lfb0801.pqm.config.PqmConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.nio.file.Path;

import static dev.lfb0801.pqm.Unchecked.Errors.suppress;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PqmConfiguration.class)
class AggregateServiceTest extends GitFSEnvironment {

	private static Path tempRepoPath;

	@Autowired
	private AggregateService aggregateService;

	@DynamicPropertySource
	static void registerProperties(DynamicPropertyRegistry registry) {
		setup(); // initializes GitFSEnvironment (remote + local)

		tempRepoPath = local.getRepository()
				.getWorkTree()
				.toPath();

		performCommits(range(0, 5)//
				.mapToObj(i -> suppress()
						.wrap(() -> {
							for (int j = 0; j < 2; j++) {
								String className = "Class" + j + ".java";
								String packageName = "com/example/module" + i;
								String path = "src/main/java/" + packageName + "/" + className;
								appendToFile(path, generateFakeJavaClass(className, i, j));
							}
						}))
				.toList());

		registry.add("quality.target.path", () -> tempRepoPath.toString());
	}

	private static String generateFakeJavaClass(String className, int module, int index) {
		return """
				package com.example.module%d;
				
				public class %s {
				    public void doSomething() {
				        System.out.println("Hello from %s #%d");
				    }
				}
				""".formatted(module, className.replace(".java", ""), className.replace(".java", ""), index);
	}

	@Test
	void testAggregateService() throws IOException {
		var result = aggregateService.aggregate();

		assertThat(result)//
				.allMatch(a -> a.locSrc() == 6)
				.allMatch(a -> a.commits()
						               .size() == 1);
	}
}
