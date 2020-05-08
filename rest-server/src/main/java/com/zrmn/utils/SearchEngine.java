package com.zrmn.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchEngine
{
    public interface Searchable
    {
        String keyWords();
    }

    public static class Searcher <T extends Searchable>
    {
        private List<T> searchables;

        public Searcher(List<T> searchables)
        {
            this.searchables = searchables;
        }

        private void filterAndFind(String[] words, Predicate<T> predicate)
        {
            Map<T, Integer> mapOfMatches = searchables.stream()
                    .filter(predicate)
                    .collect(Collectors.toMap(Function.identity(), m -> (int) Stream.of(words)
                            .filter(m.keyWords().toLowerCase()::contains)
                            .count()));

            searchables = mapOfMatches.entrySet().stream()
                    .filter(e -> e.getValue() > 0)
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        private String[] parseQuery(String query)
        {
            return query.toLowerCase().split(" ");
        }

        public Searcher find(String query)
        {
            String[] words = parseQuery(query);
            filterAndFind(words, t -> true);
            return this;
        }

        private Predicate<T> compositePredicate(List<Predicate<T>> predicates)
        {
            return predicates.stream().reduce(predicate -> true, Predicate::and);
        }

        public Searcher filter(List<Predicate<T>> predicates)
        {
            Predicate<T> compositePredicate = compositePredicate(predicates);
            searchables = searchables.stream()
                    .filter(compositePredicate)
                    .collect(Collectors.toList());
            return this;
        }

        public Searcher filterAndFind(String query, List<Predicate<T>> predicates)
        {
            String[] words = parseQuery(query);
            filterAndFind(words, compositePredicate(predicates));
            return this;
        }

        public Searcher sort(Comparator<T> comparator)
        {
            searchables.sort(comparator);
            return this;
        }

        public List<T> getSearchResults()
        {
            return searchables;
        }
    }

    public static <T extends Searchable> Searcher<T> searcher(List<T> searchables)
    {
        return new Searcher<T>(searchables);
    }
}
