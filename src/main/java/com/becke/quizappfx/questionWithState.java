package com.becke.quizappfx;

import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import javax.xml.xpath.XPath;
import java.util.List;

public class questionWithState {

    public Element question;
    public String text;
    public String[] response;
    public int correctAnswer;
    public String imageName;

    public int answer;
    public int solved;
    public int failed;
    public int id;

    questionWithState (Element question)
    {
        this.question = question;
        answer = 0;
        solved = 0;
        failed = 0;

        XPathFactory xpathFactory = XPathFactory.instance();
        XPathExpression<Object> expr1 = xpathFactory.compile("text()");
        List<Object> textl = expr1.evaluate(question);
        Text text0 = (Text)textl.get(0);
        this.text = text0.getTextTrim();

        try {
            this.id = question.getAttribute("id").getIntValue();
            this.imageName = question.getAttributeValue("img");
        } catch (DataConversionException e) {
            this.id = 0;
        }

        XPathExpression<Object> expr2 = xpathFactory.compile("response");
        List<Object> l0 = expr2.evaluate(question);
        response = new String[l0.size()];
        correctAnswer = 0;
        for (int i=0; i<l0.size(); i++) {
            Element resp0 = (Element)l0.get(i);
            response[i] = resp0.getTextTrim();
            Attribute stimmt0 = resp0.getAttribute("stimmt");
            if (stimmt0 != null && stimmt0.getValue().equals("True")) {
                correctAnswer = i+1;
            }
        }
   }

}
