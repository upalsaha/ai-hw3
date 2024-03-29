package ttr.model.player;

import java.util.Random;
import java.util.ArrayList;

import ttr.model.destinationCards.Destination;
import ttr.model.destinationCards.Route;
import ttr.model.destinationCards.Routes;
import ttr.model.trainCards.TrainCard;
import ttr.model.trainCards.TrainCardColor;

/**
 * A very stupid player that simply draws train cards only. Shown as an example of implemented a player.
 * */
public class StupidPlayer extends Player{
	
	private Routes map;
	private ArrayList<TrainCardColor> inNeed;//colors we want
	private float p;
	private TrainCardColor t;
	private int turn;
	
	
	/**
	 * Need to have this constructor so the player has a name, you can use no parameters and pass the name of your player
	 * to the super constructor, or just take in the name as a parameter. Both options are shown here.
	 * */
	public StupidPlayer(String name, float p) {
		super(name);
		this.inNeed = new ArrayList<TrainCardColor>();
		this.map = new Routes().getInstance();
		this.p = p;
		this.turn = 0;
		for ( Route r: this.map.getAllRoutes()){
			
			//System.out.println( r.toString() );
			
		}
	}
	public StupidPlayer(){
		super("Stupid Player");
	}
	
	public ArrayList<Route> getAvailableRoutes(boolean withRainbow){
		
		ArrayList<Route> returnThis = new ArrayList<Route>();
		
		int max_color = 0;	
		this.t = TrainCardColor.rainbow;
		for( TrainCardColor color: TrainCardColor.values() ){
			
			if(this.getNumTrainCardsByColor(color)>max_color){

				max_color = this.getNumTrainCardsByColor(color);
				this.t = color;
			}
		}
		//System.out.println(t.toString());
		if(!withRainbow)
		{			
			for( Route r: this.map.getAllRoutes()   ){	
				if(!this.map.isRouteClaimed(r) && !this.getPlayerClaimedRoutes().contains(r) &&r.getOwner()==null   ){ 
					
					
					if(this.map.getOwner(r) != null){
						System.out.println("who owns?  "+ this.map.ownsRoute(this.map.getOwner(r), r)  );
							
				
						
					}
					//if(r.getColor().equals(t)){
						//System.out.println("HELLO THERE! :"); 
						//if( r.getCost() <= max_color ){
						if(r.getCost() <= this.getNumTrainCardsByColor(r.getColor()) || (r.getColor().equals(TrainCardColor.rainbow) && max_color>=r.getCost() ) ){
							
							boolean flag = true;
							for (Route r_in: returnThis){
								
								if(  (false)  ||  ( r_in.getDest1().equals( r.getDest1())   &&   r_in.getDest2().equals(r.getDest2()) )  ){
									
									flag = false;	
								}
								
							}
							
							if(flag){
								returnThis.add(r);
							}
						}
					
					
				}
			}
			return returnThis;
		}
		else{
			return returnThis;
		}
		
	}
	
	/**	
	 * MUST override the makeMove() method and implement it.
	 * */
	@Override
	public void makeMove(){
		this.turn += 1;
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(101);
		ArrayList<Route> choices = new ArrayList<Route>();
		
		choices = getAvailableRoutes(false);
		/*for(Route r: choices){
			System.out.println(r.getDest1() + "<->"+r.getDest2()+":   "+r.getColor()  + "   /"+this.getNumTrainCardsByColor(r.getColor()));
		}*/
		//System.out.println(choices.toArray().length );
		
		//this.map.getNeighbors(city)
		
		Route r1 = new Route(Destination.LosAngeles , Destination.Phoenix, 3, TrainCardColor.rainbow);
		Route r2 = new Route(Destination.Nashville , Destination.Atlanta, 1, TrainCardColor.rainbow);
		
		Route r3 = new Route(Destination.Houston , Destination.NewOrleans, 2, TrainCardColor.rainbow);
		
		Route r4 = new Route(Destination.Seattle , Destination.Portland, 1, TrainCardColor.rainbow);
		
		Route r5 = new Route(Destination.Dallas , Destination.Houston, 1, TrainCardColor.rainbow);
		
		
		
		
		if(randomInt > 100*p && choices.toArray().length >= 4){

			int another  =  randomGenerator.nextInt(choices.toArray().length);
			Route same = choices.get(another);
			Route switched  = new Route(same.getDest2(), same.getDest1(), same.getCost(), same.getColor());
			
			System.out.println("trains left:  "+this.getNumTrainPieces());
			
			
			
			if(same.getColor().equals(TrainCardColor.rainbow)){
				
				super.claimRoute(switched, this.t );

				
			}
			else{
				
				super.claimRoute(switched, choices.get(another).getColor());

			}
						
		}
		else{
			super.drawTrainCard(0);
			
			
			
			
		}		/* Always draw train cards (0 means we are drawing from the pile, not from the face-up cards) */
		
		
		/* This call would allow player to draw destination tickets*/
		//super.drawDestinationTickets();
		
		/* Something like this will allow an AI to attempt to buy a route on the board. The first param is the route they wish */
		/* ...to buy, the second param is the card color they wish to pay for the route with (some routes have options here) */
		
	}
	//note: a player cannot do all three of these things in one turn;//
}
