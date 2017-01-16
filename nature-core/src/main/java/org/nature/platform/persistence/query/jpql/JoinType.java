package org.nature.platform.persistence.query.jpql;

/**
 * join类型
 * @author hutianlong
 *
 */
public enum JoinType {
	
	/**
	 * 1:JOIN
	 * 2:内连
	 * 3:左连
	 * 4:右连
	 */
		JOIN("JOIN"), 
	    INNER_JOIN("INNER JOIN"), 
	    LEFT_JOIN("LEFT JOIN"), 
	    RIGHT_JOIN("RIGHT JOIN");
	    
	    private String clausule;

	    private JoinType(String clausule) {
	        this.clausule = clausule;
	    }
	    
	    public String getClausule() {
	        return clausule;
	    }
	    
}
