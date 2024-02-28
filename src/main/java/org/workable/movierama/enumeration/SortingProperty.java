package org.workable.movierama.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

/**
 * Enum representing the sorting properties for a list, which could include sorting by likes, hates, or date.
 */
@RequiredArgsConstructor
@Getter
public enum SortingProperty {

    /**
     * Sorting by the number of likes.
     */
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

    /**
     * Sorting by the number of hates.
     */
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

    /**
     * Sorting by the date added.
     */
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

    /**
     * Returns the corresponding {@link SortingProperty} based on the provided sorting method.
     *
     * @param sortBy the method to sort by
     * @return the {@link SortingProperty} enum
     * @throws IllegalArgumentException if the sorting method is not supported
     */
    public static SortingProperty of(String sortBy) {
        return Arrays.stream(values())
                .filter(property -> property.getSortBy().equalsIgnoreCase(sortBy))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Sorting method by: %s is not supported", sortBy)));
    }

    /**
     * Provides the {@link PageRequest} necessary for pagination based on the sorting property.
     * @param page the current page number
     * @param pageSize the size of the page
     * @return a {@link PageRequest} configured with the appropriate sorting
     */
    public abstract PageRequest getPageRequest(Integer page, Integer pageSize);

    /**
     * Returns the {@link OpinionType} associated with the sorting property.
     * @return the {@link OpinionType}
     * @throws UnsupportedOperationException if the sorting property does not have an associated opinion type
     */
    public abstract OpinionType getOpinionType();

}
