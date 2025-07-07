package services.sorting;

/**
 *
 * @author HieuTT
 */
public class MostRecentSort implements ReviewSortStrategy {

    @Override
    public String getOrderByClause() {
        return " date desc";
    }
}
