package com.zero.main;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.zero.spaceshooter.actors.ManagerActor;


public class PolygonParser {

	private Document doc = null;
	private ArrayList<FixtureDef> fixtures = null;

	public void load(String name) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(Gdx.files.internal("res/" +name+ ".xml").file()  );
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		fixtures = new ArrayList<FixtureDef>(20);
		prepareFixtures();
	}

	private void prepareFixtures() {
		NodeList nList = doc.getElementsByTagName("polygon");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				NodeList verticesList = eElement.getElementsByTagName("vertex");
				Vector2[] vertices = new Vector2[verticesList.getLength()];
				for (int j = 0; j < verticesList.getLength(); j++) {
					Node xNode = verticesList.item(j).getAttributes().item(0);
					Node yNode = verticesList.item(j).getAttributes().item(1);
					Float x = Float.valueOf(xNode.getNodeValue().trim()).floatValue();
					Float y = Float.valueOf(yNode.getNodeValue().trim()).floatValue();
					vertices[j] = new Vector2(x/ManagerActor.PTM, y/ManagerActor.PTM);
				}

				PolygonShape shape = new PolygonShape();
				shape.set(vertices);

				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape = shape;
				fixtureDef.density = 600f;
				fixtureDef.friction = 0f;
				fixtureDef.restitution = 0.3f;
				fixtureDef.filter.maskBits = (short)0xFFFF;
				fixtures.add(fixtureDef);
			}
		}
	}

	public void atachFixtures(Body body, short filterBit) {
		for (FixtureDef fixtureDef : fixtures) {
			fixtureDef.filter.categoryBits = filterBit;
			body.createFixture(fixtureDef);
		}
	}
}
