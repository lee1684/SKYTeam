package kr.co.ssalon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TextBox extends TicketJsonObject{
    private String text;
    private boolean underline;
    private boolean overline;
    private boolean linethrough;
    private String textAlign;
    private String fontFamily;
    private String fontWeight;
    private double fontSize;
    private double lineHeight;
    private String textBackgroundColor;
    private double charSpacing;
    private List<String> styles;
    private String direction;
    private String path;
    private double pathStartOffset;
    private String pathSide;
    private String pathAlign;
    private double minWidth;
    private boolean splitByGrapheme;
}
