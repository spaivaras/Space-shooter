package com.zero.main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.pooling.arrays.Vec2Array;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PolygonParser {

	public void parseEntity(String name, Body body) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse("res/"+name+".xml");
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("polygon");

			Vec2[] vertices = new Vec2Array().get(8);
			FixtureDef fixtureDef = new FixtureDef();

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					NodeList verticesList = eElement.getElementsByTagName("vertex");
					for (int j = 0; j < verticesList.getLength(); j++) {
						Node xNode = verticesList.item(j).getAttributes().item(0);
						Node yNode = verticesList.item(j).getAttributes().item(1);
						Float x = Float.valueOf(xNode.getNodeValue().trim()).floatValue();
						Float y = Float.valueOf(yNode.getNodeValue().trim()).floatValue();
						vertices[j].set(new Vec2(x, y));
					}

					PolygonShape shape = new PolygonShape();
					shape.set(vertices, verticesList.getLength());

					fixtureDef.shape = shape;
					fixtureDef.density = 0.001f;
					fixtureDef.friction = 0f;
					fixtureDef.restitution = 0.3f;
					body.createFixture(fixtureDef);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
