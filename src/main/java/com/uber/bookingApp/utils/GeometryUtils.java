package com.uber.bookingApp.utils;

import com.uber.bookingApp.dto.PointDto;
import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@UtilityClass
public class GeometryUtils {

    public static Point buildPoint(PointDto pointDto){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(pointDto.getLongitude(), pointDto.getLatitude());
        return geometryFactory.createPoint(coordinate);

    }
}
