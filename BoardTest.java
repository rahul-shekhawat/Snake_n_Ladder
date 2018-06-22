package com.qainfotech.tap.training.snl.api;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 

public class BoardTest {
	
	
	Board obj;
	JSONObject data;
	BoardModel model;
	
	
	
	@Test(expectedExceptions={MaxPlayersReachedExeption.class})
	public void trying_to_add_more_than_four_players() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		obj.registerPlayer("Player1");
		obj.registerPlayer("Player2");
		obj.registerPlayer("Player3");
		obj.registerPlayer("Player4");
		obj.registerPlayer("Player5");	
		
	}
	
	@Test(expectedExceptions={PlayerExistsException.class})
	public void try_adding_same_name_users() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		obj.registerPlayer("Player1");
		obj.registerPlayer("Player1");
		
	}
	
	@Test(expectedExceptions={GameInProgressException.class})
	public void try_adding_user_after_gameinprogress() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, JSONException, InvalidTurnException
	{
		obj.registerPlayer("Player1");
		obj.registerPlayer("Player2");
		JSONArray array=new JSONArray();
		array=obj.registerPlayer("Player3");
		//array=data.getJSONArray("players");
		
		String sid;
		
		for(int i = 0; i < array.length(); i++){
            JSONObject player = array.getJSONObject(i);
            sid=(String) player.get("uuid");
            UUID id=UUID.fromString(sid);
            obj.rollDice(id);
		}
		
		obj.registerPlayer("Player4");
	}
	
	@Test(expectedExceptions={NoUserWithSuchUUIDException.class})
	public void deleting_user_not_existing() throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException
	{
		UUID invalid=UUID.randomUUID();
		obj.deletePlayer(invalid);
	}
	
	@Test(expectedExceptions={InvalidTurnException.class})
	public void making_invalid_turn() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, JSONException, InvalidTurnException
	{
		
		String sid;
		obj.registerPlayer("Player1");
		obj.registerPlayer("Player2");
		JSONArray array=new JSONArray();
		array=obj.registerPlayer("Player3");
		//array=data.getJSONArray("players");
		
		
		for(int i =array.length()-1;i>=0; i--){
            JSONObject player = array.getJSONObject(i);
            sid=(String) player.get("uuid");
            UUID id=UUID.fromString(sid);
            obj.rollDice(id);
		}
	}
	
	@BeforeMethod
	public void launchwindow() throws FileNotFoundException, UnsupportedEncodingException, JSONException, IOException
	{
		
		obj=new Board();
		data=new JSONObject();
		model=new BoardModel();
		
	}
	

}
