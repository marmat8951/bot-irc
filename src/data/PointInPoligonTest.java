package data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PointInPoligonTest {
	Coordinates a = new Coordinates(2,3);
	Coordinates b = new Coordinates(3, -1);
	Coordinates c = new Coordinates(-2, -2);
	Coordinates d = new Coordinates(-3, 1);
	Coordinates c1 = new Coordinates(2, 2);
	Coordinates c2 = new Coordinates(2.5, 2);
	Coordinates c3 = new Coordinates(-2.26, -0.82);
	Coordinates c4 = new Coordinates(-2.77, 0.96);
	Coordinates c5 = new Coordinates(-3.03, 0.82);
	Coordinates c6 = new Coordinates(1.95, -0.79);
	Coordinates c7 = new Coordinates(-0.36, 2.17);
	Coordinates c8 = new Coordinates(3.31, -0.72);
	Coordinates c9 = new Coordinates(-0.42, -1.74);
	Coordinates c10 = new Coordinates(0.37, 3.75);
	Coordinates c11 = new Coordinates(0, 0);
	Coordinates c12 = new Coordinates(0, -1.36);
	

	@Test
	public void testP1P() {
		Polygon p = new Polygon();
		p.add(a);
		p.add(b);
		p.add(c);
		p.add(d);
		
		
		
		System.out.println("===== C1 ====="+true);
		assertTrue(p.isInPolygon(c1));
		System.out.println("===== C2 ====="+false);
		assertFalse(p.isInPolygon(c2));
		System.out.println("===== C3 ====="+true);
		assertTrue(p.isInPolygon(c3));
		System.out.println("===== C4 ====="+true);
		assertTrue(p.isInPolygon(c4));
		System.out.println("===== C5 ====="+false);
		assertFalse(p.isInPolygon(c5));
		System.out.println("===== C6 ====="+true);
		assertTrue(p.isInPolygon(c6));
		System.out.println("===== C7 ====="+false);
		assertFalse(p.isInPolygon(c7));
		System.out.println("===== C8 ====="+false);
		assertFalse(p.isInPolygon(c8));
		System.out.println("===== C9 ====="+false);
		assertFalse(p.isInPolygon(c9));
		System.out.println("===== C10 ====="+false);
		assertFalse(p.isInPolygon(c10));
		System.out.println("===== C11 ====="+true);
		assertTrue(p.isInPolygon(c11));
		System.out.println("===== C12 ====="+true);
		assertTrue(p.isInPolygon(c12));
	}
	
	@Test
	public void distanceAvecTest() {
		assertEquals(1.85, c3.distanceWithPoint(c4),0.1);
		assertEquals(1.85, c4.distanceWithPoint(c3),0.1);
		assertEquals(2.2, c11.distanceWithPoint(c7),0.1);
		assertEquals(5.17, c5.distanceWithPoint(c1),0.1);
		assertEquals(4.74, c9.distanceWithPoint(c2),0.1);
		assertEquals(5.32, c8.distanceWithPoint(c10),0.1);
		assertEquals(2.03,c12.distanceWithPoint(c6),0.1);
		
	}
	
	@Test
	public void longitudeCorrespondingTotheLatTest() {
		Segment s1 = new Segment(c, d);
		assertEquals(-0.5, s1.getLongitudeCorrespondingToTheLatitude(-2.5), 0.1);
		Segment s2 = new Segment(a, b);
		assertEquals(2.5, s2.getLongitudeCorrespondingToTheLatitude(1),0.1);
	}

}
