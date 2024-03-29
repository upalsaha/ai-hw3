package ttr.model.player;



import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ttr.model.destinationCards.Destination;
import ttr.model.destinationCards.DestinationTicket;
import ttr.model.destinationCards.Route;
import ttr.model.destinationCards.Routes;
import ttr.model.trainCards.TrainCard;
import ttr.model.trainCards.TrainCardColor;

/**
 * A very stupid player that simply draws train cards only. Shown as an example of implemented a player.
 * */
public class Guwop extends Player{
	
	private Routes map;
	private ArrayList<TrainCardColor> inNeed;//colors we want
	private float p;
	private TrainCardColor t;
	private int turn;
	

	public ArrayList<Destination> dijkstras(Destination start, Destination dest){
		
		ArrayList<Destination> path = new ArrayList<Destination>();
		
		ArrayList<Destination> q = new ArrayList<Destination>();
		
		HashMap<Destination, Integer> dist  = new HashMap<Destination, Integer>();

		HashMap<Destination, Destination> prev  = new HashMap<Destination, Destination>();
		
		
		for (Destination d: Destination.values()){
			
			dist.put(d, 999999);
			prev.put(d, null);
			q.add(d);
		}
		dist.put(start, 0);
		
		boolean flag = false;

		Destination min_key = null;
		
		while(! q.isEmpty() ){
			
			int current = 1000000;
			for(Destination keys: q){
				if(dist.get(keys)  < current ){
					current  = dist.get(keys);
					min_key = keys;
				}
			}
			if(min_key.equals(dest)){
				flag =  true;
				break;
			}

			//System.out.println(min_key);
			
			//System.out.println(q.size());
			//System.out.println(q.contains(min_key));
			
			//System.out.println(q);			
			q.remove(min_key);

			//System.out.println(q.size());
			int alternative_path = 0;
			for( Destination neighbor: this.map.getNeighbors(min_key) ){
				
			
				alternative_path = dist.get(min_key) + this.map.shortestPathcost(neighbor, min_key);
				if(alternative_path < dist.get(neighbor) ){
					dist.replace(neighbor, alternative_path);
					prev.replace(neighbor, min_key);
				}
			}
		}
		
		if(flag){
			
			Destination u = dest;
			while( prev.get(u)!=null ){
				
				path.add(0, u);
				u = prev.get(u);
			}
			path.add(0,u);
			return path;
			
			
		}
		else{

			return path;
		}
	}
	/**
	 * Need to have this constructor so the player has a name, you can use no parameters and pass the name of your player
	 * to the super constructor, or just take in the name as a parameter. Both options are shown here.
	 * */
	
	public Guwop(String name){
		super(name);
		this.inNeed = new ArrayList<TrainCardColor>();
		this.map = new Routes().getInstance();
		this.p = 0.5f;
		this.turn = 0;
	}
	
	
	public ArrayList<Route> getAlongDijkstra(){
		
		ArrayList<Route> master = new ArrayList<Route>();
		
		for( DestinationTicket d :this.getDestinationTickets()){
			
			ArrayList<Destination> path = new ArrayList<Destination>();
			ArrayList<Route> route_path = new ArrayList<Route>();
			path = this.dijkstras(d.getTo(), d.getFrom());
			for(int i=0;i<path.toArray().length-1;i+=1){
				for( Route alongside :this.map.getRoutes(path.get(i), path.get(i+1))){
					route_path.add(alongside);
					master.add(alongside);
				}
			}			
		}
		return master;
	}
	
	public ArrayList<Route> getAvailableRoutes(boolean withRainbow){
		
		ArrayList<Route> returnThis = new ArrayList<Route>();
		ArrayList<Route> dijk = new ArrayList<Route>();
		dijk = getAlongDijkstra();
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
						if(r.getCost() <= this.getNumTrainCardsByColor(r.getColor())+this.getNumTrainCardsByColor(TrainCardColor.rainbow) || (r.getColor().equals(TrainCardColor.rainbow) && max_color>=r.getCost() ) ){
							
							
							
							boolean flag = true;
							for (Route r_in: returnThis){
								
								if(  (false)  ||  ( r_in.getDest1().equals( r.getDest1())   &&   r_in.getDest2().equals(r.getDest2()) )  ){
									
									flag = false;	
								}
								
							}
							
							if(flag){
								
							
								if(dijk.contains(r) || (this.turn > 60 && r.getPoints()>=5 && this.getDestinationTickets().toArray().length <= 3 ) ){
									
								
									returnThis.add(r);
								}
							}
							
							
						}
					
					
				}
			}
			if(returnThis.isEmpty()){
				
				
				
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
		if (this.turn ==1){
			ArrayList<Route> path = new ArrayList<Route>();
			System.out.println("dijstras started");
			path = this.getAlongDijkstra();
			
			System.out.println("dijstras finished");
			for(Route city: path){
			System.out.println(city.toString());
			//this.drawDestinationTickets();
			}
			
		}
		if (this.turn > 24 ){
			

			this.p = 0.0f;
			
		}
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(101);
		ArrayList<Route> choices = new ArrayList<Route>();
		
		choices = getAvailableRoutes(false);
		//choices = getAlongDijkstra();
		for(Route r: choices){
			System.out.println(r.getDest1() + "<->"+r.getDest2()+":   "+r.getColor()  + "   /"+this.getNumTrainCardsByColor(r.getColor()));
		}
		//System.out.println(choices.toArray().length );
		
		//this.map.getNeighbors(city)
		
		Route r1 = new Route(Destination.LosAngeles , Destination.Phoenix, 3, TrainCardColor.rainbow);
		Route r2 = new Route(Destination.Nashville , Destination.Atlanta, 1, TrainCardColor.rainbow);
		
		Route r3 = new Route(Destination.Houston , Destination.NewOrleans, 2, TrainCardColor.rainbow);
		
		Route r4 = new Route(Destination.Seattle , Destination.Portland, 1, TrainCardColor.rainbow);
		
		Route r5 = new Route(Destination.Dallas , Destination.Houston, 1, TrainCardColor.rainbow);
		
		if(turn < 14){
			choices = new ArrayList<Route>();
			if(! this.map.isRouteClaimed(r1)){
				choices.add(r1);}

				if(! this.map.isRouteClaimed(r2)){
				choices.add(r2);}

				if(! this.map.isRouteClaimed(r3)){
				choices.add(r3);}

				if(! this.map.isRouteClaimed(r4)){
				choices.add(r4);}

				if(! this.map.isRouteClaimed(r5)){
				choices.add(r5);}
			
		}
		
		
		if(randomInt > 100*p && choices.toArray().length >= 1){

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
