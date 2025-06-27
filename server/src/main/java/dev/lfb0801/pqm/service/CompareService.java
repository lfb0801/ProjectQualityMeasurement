package dev.lfb0801.pqm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.springframework.stereotype.Service;

import dev.lfb0801.pqm.domain.Aggregate;
import dev.lfb0801.pqm.domain.Comparing;

@Service
public class CompareService {

    private final AggregateService aggregateService;
    private final Git git;

    public CompareService(AggregateService aggregateService, Git git) {
        this.aggregateService = aggregateService;
        this.git = git;
    }

    private static List<Comparing<?>> applySequentialMappers(Comparing<?> initial, List<Function<?, ?>> mappers) {
        return Stream.iterate(new ArrayList<Comparing<?>>(List.of(initial)),//
                              list -> list.size() <= mappers.size(),//
                              list -> {
                                  list.add(applyMapper(list.getLast(), mappers.get(list.size() - 1)));
                                  return list;
                              }
            )
            .reduce((a, b) -> b)
            .orElseThrow();
    }

    @SuppressWarnings("unchecked")
    private static <A, B> Comparing<B> applyMapper(Comparing<A> previous, Function<?, ?> mapper) {
        return previous.map((Function<A, B>) mapper);
    }

    public List<Comparing<?>> compareTags(Comparing<String> comparing) {
        List<Function<?, ?>> mappers = //
            List.of(//
                    (Function<String, String>) this::findTagMatching,  //
                    (Function<String, ObjectId>) this::resolveHash, //
                    (Function<ObjectId, Set<Aggregate>>) aggregateService::aggregate //
            );

        return applySequentialMappers(comparing, mappers);
    }

    public String findTagMatching(String name) {
        try {
            final var tagList = git.tagList()
                .call();
            return tagList.stream()
                .map(Ref::getName)
                .filter(s -> s.contains(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find branch:{%s}, in list:{%s}".formatted(name, tagList)));
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectId resolveHash(String revstr) {
        try {
            return git.getRepository()
                .resolve(revstr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
