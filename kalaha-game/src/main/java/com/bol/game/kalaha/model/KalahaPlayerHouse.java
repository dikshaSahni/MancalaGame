package com.bol.game.kalaha.model;

public class KalahaPlayerHouse extends KalahaPlayerPit {
	private static final long serialVersionUID = 1L;

	public KalahaPlayerHouse(Integer id) {
        super(id , 0);
    }
	
	public KalahaPlayerHouse(Integer id, int stones) {
        super(id , stones);
    }
}
