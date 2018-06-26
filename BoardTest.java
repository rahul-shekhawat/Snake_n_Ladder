package com.qainfotech.tap.training.snl.api;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
	
	@Test(expectedExceptions={NoUserWithSuchUUIDException.class})
	public void checking_successful_deletion() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, NoUserWithSuchUUIDException
	{
		obj.registerPlayer("Player1");
		obj.registerPlayer("Player2");
		JSONObject data=new JSONObject();
		data=obj.getData();
		JSONArray array=new JSONArray();
		array=data.getJSONArray("players");
		JSONObject playertodelete = array.getJSONObject(0);
		UUID uuid=(UUID) playertodelete.get("uuid");
		obj.deletePlayer(uuid);
		obj.deletePlayer(uuid);
		
	}
	
	@Test(expectedExceptions={PlayerExistsException.class})
	public void checking_successful_registeration() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException
	{
		obj.registerPlayer("Player1");
		JSONObject data=new JSONObject();
		data=obj.getData();
		JSONArray array=new JSONArray();
		array=data.getJSONArray("players");
		JSONObject playertoregister = array.getJSONObject(0);
		String playername=playertoregister.getString("name");
		obj.registerPlayer(playername);
		
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
	
	@Test
	public void checkingrenderingofcorrectmessages() throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException, JSONException, InvalidTurnException
	{	
		JSONArray array=new JSONArray();
		obj.registerPlayer("Player1");
		array=obj.registerPlayer("Player2");
		
		for(int i=0;i<2;i++) {
			JSONObject player = array.getJSONObject(i);
			String id = (String)player.get("uuid"); 
			UUID playerUuid = UUID.fromString(id);
			int currentPosition = (int)obj.data.getJSONArray("players").getJSONObject(i).get("position");
			JSONObject response = obj.rollDice(playerUuid);
			int diceValue = response.getInt("dice");
			int type = (int) obj.data.getJSONArray("steps").getJSONObject(currentPosition+diceValue).get("type");
			int newPosition = (int)obj.data.getJSONArray("players").getJSONObject(i).get("position");
			if(type==0)
				Assert.assertTrue(response.get("message").equals("Player moved to " + newPosition));
			else if(type==1)
				Assert.assertTrue(response.get("message").equals("Player was bit by a snake, moved back to " + newPosition));
			else if(type==2)
				Assert.assertTrue(response.get("message").equals("Player climbed a ladder, moved to " + newPosition));
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
