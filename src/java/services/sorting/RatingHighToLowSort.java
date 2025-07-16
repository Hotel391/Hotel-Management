package services.sorting;

/**
 *
 * @author HieuTT
 */
public class RatingHighToLowSort implements ReviewSortStrategy{

    @Override
    public String getOrderByClause() {
        return "rating desc";
    }
    
}
