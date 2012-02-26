package com.zero.main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;


public class PolygonParser {

	public void parseEntity(String name, Body body, short filterBit) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(Gdx.files.internal("res/" +name+ ".xml").file()  );
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("polygon");
			FixtureDef fixtureDef = new FixtureDef();

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
						vertices[j] = new Vector2(x/32, y/32);
					}

					PolygonShape shape = new PolygonShape();
					shape.set(vertices);

					fixtureDef.shape = shape;
					fixtureDef.density = 600f;
					fixtureDef.friction = 0f;
					fixtureDef.restitution = 0.3f;
					fixtureDef.filter.categoryBits = filterBit;
					fixtureDef.filter.maskBits = (short)~filterBit;
					
					body.createFixture(fixtureDef);
					shape.dispose();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
