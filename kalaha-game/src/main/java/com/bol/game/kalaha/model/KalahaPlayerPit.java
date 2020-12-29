package com.bol.game.kalaha.model;

import java.io.Serializable;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@UserDefinedType("player_pit")
public class KalahaPlayerPit implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKeyColumn(value = "pit_id")
	private Integer pitId;
	
	@Column(value = "stones")
	private Integer numberOfStones;
	
	public void dropOneStone() {
        this.numberOfStones++;
    }

	public boolean isEmpty() {
		return (this.numberOfStones == 0);
	}

	public void takeAllStones() {
		this.numberOfStones = 0;
	}

	public void addStones(int newStones) {
		this.numberOfStones += newStones;
	}
	
	@Override
    public String toString() {
        return  pitId.toString() +
                ":" +
                numberOfStones.toString() ;
    }
	
}
