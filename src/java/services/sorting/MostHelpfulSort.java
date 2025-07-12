package services.sorting;

/**
 *
 * @author HieuTT
 */
public class MostHelpfulSort implements ReviewSortStrategy{

    @Override
    public String getOrderByClause() {
        return "LEN(FeedBack) desc";
    }
    
}
