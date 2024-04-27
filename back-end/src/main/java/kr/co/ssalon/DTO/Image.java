package kr.co.ssalon.DTO;

import lombok.Data;

import java.util.List;

@Data
public class Image extends TicketJsonObject{
    private double cropX;
    private double cropY;
    private String src;
    private String crossOrigin;
    private List<Object> filters;
}
