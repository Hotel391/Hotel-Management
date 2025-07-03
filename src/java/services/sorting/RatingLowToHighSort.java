package services.sorting;

/**
 *
 * @author HieuTT
 */
public class RatingLowToHighSort implements ReviewSortStrategy{

    @Override
    public String getOrderByClause() {
        return "rating asc";
    }
    
}
