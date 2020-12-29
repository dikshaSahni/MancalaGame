package com.bol.game.kalaha.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("player_list")
public enum KalahaPlayerList {

    PlayerOne ("One"),
    PlayerTwo ("Two");

    private String turn;

    KalahaPlayerList(String turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return turn;
    }


}
