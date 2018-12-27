package bot.irc.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Polygon {
	private List<Coordinates> points;
	
	public Polygon(){
		points = new ArrayList<>();
	}
	
	public Polygon(List<Coordinates> points) {
		this.points = points;
	}
	
	public boolean addPoint(Coordinates point) {
		return this.points.add(point);
	}
	
	

	public boolean isEmpty() {
		return points.isEmpty();
	}

	public boolean add(Coordinates e) {
		return points.add(e);
	}

	public boolean addAll(Collection<? extends Coordinates> c) {
		return points.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends Coordinates> c) {
		return points.addAll(index, c);
	}

	public void add(int index, Coordinates element) {
		points.add(index, element);
	}

	public List<Coordinates> getPoints() {
		return points;
	}

	public void setPoints(List<Coordinates> points) {
		this.points = points;
	}
	
	public int getPointsNumber() {
		return points.size();
	}
	
	public Segment getSegment(int id) {
		int size = points.size();
		return new Segment(points.get(id%size),points.get((id+1)%size));
	}
	
	public List<Segment> getAllSegmentsOfThePolygonForTheLatitude(double latitude){
		List<Segment> res = new LinkedList<>();
		for(int i=0; i<points.size(); ++i) {
			Segment s = getSegment(i);
			if(s.isLatitudeBeetweenTheTwoPoints(latitude)) {
				res.add(s);
			}
		}
		return res;
	}
	
	public boolean isInPolygon(Coordinates coordinates) {
		List<Segment> segmentsInTheLatitude =  getAllSegmentsOfThePolygonForTheLatitude(coordinates.getLatitude());
		int segmentsCrossed = 0;
		for(Segment s : segmentsInTheLatitude) {
			double longitude = s.getLongitudeCorrespondingToTheLatitude(coordinates.getLatitude());
			if(longitude<=coordinates.getLongitude()) segmentsCrossed++;
		}
		return (segmentsCrossed / 2) == 1;  //nombre impair de croisement de segments: on est dans le polygone.
	}
	
	
}
