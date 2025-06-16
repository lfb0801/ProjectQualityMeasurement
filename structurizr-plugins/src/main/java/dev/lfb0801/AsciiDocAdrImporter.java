package dev.lfb0801;

import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Format;
import com.structurizr.importer.documentation.AdrToolsDecisionImporter;
import com.structurizr.importer.documentation.DocumentationImportException;
import com.structurizr.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class AsciiDocAdrImporter extends AdrToolsDecisionImporter {

	private static final String DATE_PREFIX = ":date: ";
	private static final String STATUS_HEADING = ":status: ";
	private static final String CONTEXT_HEADING = "## Context";
	private static final Pattern LINK_REGEX = Pattern.compile("link:([^\\[]+)\\[([^]]+)]");

	@Override
	public void importDocumentation(Documentable documentable, File path) {
		if (documentable == null) {
			throw new IllegalArgumentException(
					"A workspace, software system, container, or component must be specified.");
		} else if (path == null) {
			throw new IllegalArgumentException("A path must be specified.");
		} else if (!path.exists()) {
			throw new IllegalArgumentException(path.getAbsolutePath() + " does not exist.");
		} else if (!path.isDirectory()) {
			throw new IllegalArgumentException(path.getAbsolutePath() + " is not a directory.");
		} else {
			super.importDocumentation(documentable, path);

			Optional.ofNullable(path.listFiles((dir, name) -> name.endsWith(".adoc")))
					.stream()
					.flatMap(Arrays::stream)
					.forEach(file -> {
						Decision decision = null;
						try {
							decision = this.importDecision(file);
							documentable.getDocumentation()
									.addDecision(decision);
						} catch (Exception e) {
							throw new DocumentationImportException(e);
						}
					});
		}
	}

	protected Decision importDecision(File file) throws Exception {
		var id = this.extractIntegerIDFromFileName(file);
		var decision = new Decision(id);
		var content = Files.readString(file.toPath(), this.characterEncoding);
		content = content.replace("\r", "");
		decision.setContent(content);
		var lines = content.split("\\n");
		decision.setTitle(this.extractTitle(lines));
		decision.setDate(this.extractDate(lines));
		decision.setStatus(this.extractStatus(lines));
		decision.setFormat(Format.AsciiDoc);
		return decision;
	}

	protected String extractTitle(String[] lines) {
		return Arrays.stream(lines)
				.filter(line -> line.startsWith("="))
				.findFirst()
				.or(() -> Optional.of(lines[0]))
				.map(s -> {
					var ll = new LinkedList<>(List.of(s.split(" ")));
					ll.removeFirst();
					return String.join(" ", ll);
				})
				.orElseThrow();
	}

	protected Date extractDate(String[] lines) throws Exception {
		var sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(this.timeZone);

		return Arrays.stream(lines)
				.filter(line -> line.startsWith(DATE_PREFIX))
				.findFirst()
				.map(line -> line.substring(DATE_PREFIX.length()))
				.map(source -> {
					try {
						return sdf.parse(source);
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
				})
				.orElse(new Date());
	}

	protected String extractStatus(String[] lines) {
		return Arrays.stream(lines)
				.filter(line -> line.startsWith(STATUS_HEADING))
				.map(line -> line.substring(STATUS_HEADING.length()))
				.findFirst()
				.orElse("Proposed");
	}

	protected void extractLinks(Decision decision, Map<String, Decision> decisionsByFilename) {
		var lines = decision.getContent()
				.split("\\n");

		for (var line : lines) {
			if (!StringUtils.isNullOrEmpty(line)) {
				var matcher = LINK_REGEX.matcher(line);
				if (matcher.find()) {
					var target = matcher.group(1)
							.trim();
					var linkDescription = matcher.group(2)
							.trim();
					var targetDecision = decisionsByFilename.get(target);
					if (targetDecision != null) {
						decision.addLink(targetDecision, linkDescription);
					}
				}
			}
		}

	}
}
