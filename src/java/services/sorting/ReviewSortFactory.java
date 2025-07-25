package services.sorting;

/**
 *
 * @author HieuTT
 */
public class ReviewSortFactory {

    private ReviewSortFactory() {

    }

    public static ReviewSortStrategy getStrategy(String sortOption) {
        switch (sortOption) {
            case "recent":
                return new MostRecentSort();
            case "rating_high":
                return new RatingHighToLowSort();
            case "rating_low":
                return new RatingLowToHighSort();
            case "helpful":
                return new MostHelpfulSort();
            default:
                return new MostRecentSort();
        }
    }
}
