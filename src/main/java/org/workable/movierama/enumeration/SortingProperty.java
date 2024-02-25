package org.workable.movierama.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum SortingProperty {

    BY_LIKES("likes", true) {
        @Override
        public PageRequest getPageRequest(Integer page, Integer pageSize) {
            return PageRequest.of(page, pageSize, Sort.by("likes"));
        }

        @Override
        public OpinionType getOpinionType() {
            return OpinionType.LIKE;
        }
    },
    BY_HATES("hates", true) {
        @Override
        public PageRequest getPageRequest(Integer page, Integer pageSize) {
            return PageRequest.of(page, pageSize, Sort.by("hates"));
        }

        @Override
        public OpinionType getOpinionType() {
            return OpinionType.HATE;
        }
    },
    BY_DATE("date", false) {
        @Override
        public PageRequest getPageRequest(Integer page, Integer pageSize) {
            return PageRequest.of(page, pageSize, Sort.by("dateAdded"));
        }

        @Override
        public OpinionType getOpinionType() {
            throw new UnsupportedOperationException("By date sorting does not have an opinion type.");
        }
    };

    private final String sortBy;
    private final Boolean hasOpinion;

    public static SortingProperty of(String sortBy) {
        return Arrays.stream(values())
                .filter(property -> property.getSortBy().equalsIgnoreCase(sortBy))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Sorting method by: %s is not supported", sortBy)));
    }

    public abstract PageRequest getPageRequest(Integer page, Integer pageSize);

    public abstract OpinionType getOpinionType();

}
