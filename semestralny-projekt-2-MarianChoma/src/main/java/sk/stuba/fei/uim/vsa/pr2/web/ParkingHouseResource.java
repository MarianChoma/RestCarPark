package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;
import sk.stuba.fei.uim.vsa.pr2.service.FloorService;
import sk.stuba.fei.uim.vsa.pr2.service.ParkingHouseService;
import sk.stuba.fei.uim.vsa.pr2.service.SpotService;
import sk.stuba.fei.uim.vsa.pr2.web.response.FloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingHouseDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.SpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.FloorResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingHouseResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.SpotResponseFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/carparks")
public class ParkingHouseResource {
    private final ParkingHouseService parkingHouseService = new ParkingHouseService();
    private final FloorService floorService = new FloorService();
    private final SpotService spotService = new SpotService();
    private final ParkingHouseResponseFactory parkingHouseFactory = new ParkingHouseResponseFactory();
    private final FloorResponseFactory floorFactory = new FloorResponseFactory();
    private final SpotResponseFactory spotFactory = new SpotResponseFactory();
    private final ObjectMapper json = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCarParks(@QueryParam("name") @DefaultValue("") String name) {
        if (name.isEmpty()) {
            List<ParkingHouse> parkingHouseList = parkingHouseService.getCarParks();
            List<ParkingHouseDto> parkingHouseDtos = parkingHouseList.stream().map(parkingHouseFactory::transformToDto).collect(Collectors.toList());
            try {
                return json.writeValueAsString(parkingHouseDtos);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        } else {
            ParkingHouse p = parkingHouseService.getCarParks(name);
            ParkingHouseDto pDto = parkingHouseFactory.transformToDto(p);
            try {
                return json.writeValueAsString(pDto);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCarPark(String body) {
        try {
            ParkingHouse parkingHouse = json.readValue(body, ParkingHouse.class);
            List<Floor> floors = parkingHouse.getFloors();
            parkingHouse.setFloors(new ArrayList<>());
            parkingHouseService.createCarPark(parkingHouse);
            if (floors != null) {
                for (Floor floor : floors) {
                    floor.setCarPark(parkingHouse);
                    List<ParkingPlace> parkingPlaces = floor.getSpots();
                    floor.setSpots(new ArrayList<>());
                    floorService.createCarParkFloor(floor);
                    if (parkingPlaces != null) {
                        for (ParkingPlace p : parkingPlaces) {
                            p.setCarPark(parkingHouse);
                            p.setCarParkFloor(floor);
                            spotService.createParkingSpot(p);
                        }
                        floor.setSpots(parkingPlaces);
                        floorService.updateCarParkFloor(floor,floor.getIdentifier());
                    }
                }
            }
            parkingHouse.setFloors(floors);
            parkingHouseService.updateCarPark(parkingHouse);
            ParkingHouseDto parkingHouseDto = parkingHouseFactory.transformToDto(parkingHouse);

            return Response.status(201)
                    .entity(json.writeValueAsString(parkingHouseDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCarParkById(@PathParam("id") Long id) {
        try {
            ParkingHouse p = parkingHouseService.getCarPark(id);
            if (p == null) {
                return "{}";
            }
            ParkingHouseDto parkingHouseDto = parkingHouseFactory.transformToDto(p);
            return json.writeValueAsString(parkingHouseDto);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        try {
            parkingHouseService.deleteCarPark(id);
            return Response.status(204).build();
        } catch (Exception e) {
            return Response.status(400).build();
        }

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCarPark(@PathParam("id") Long id, String body) {
        try {
            ParkingHouse parkingHouse = json.readValue(body, ParkingHouse.class);
            parkingHouse.setId(id);
            try {
                parkingHouse = parkingHouseService.updateCarPark(parkingHouse);
            } catch (Exception e) {
                return Response.status(400).build();
            }

            ParkingHouseDto parkingHouseDto = parkingHouseFactory.transformToDto(parkingHouse);

            return Response.status(200)
                    .entity(json.writeValueAsString(parkingHouseDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFloors(@PathParam("id") Long id) {
        List<Floor> carParkFloors = floorService.getCarParkFloors(id);
        List<FloorDto> floorDtos = carParkFloors.stream().map(floorFactory::transformToDto).collect(Collectors.toList());
        try {
            return json.writeValueAsString(floorDtos);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @POST
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFloor(@PathParam("id") Long id, String body) {
        try {
            Floor floor = json.readValue(body, Floor.class);
            ParkingHouse p = parkingHouseService.getCarPark(id);
            floor.setCarPark(p);
            FloorDto floorDto;
            try {
                List<ParkingPlace> parkingSpots = floor.getSpots();
                floor.setSpots(new ArrayList<>());
                floorService.createCarParkFloor(floor);
                if(parkingSpots!=null){
                    for(ParkingPlace spot : parkingSpots){
                        spot.setCarPark(p);
                        spot.setCarParkFloor(floor);
                        spotService.createParkingSpot(spot);
                    }
                    floor.setSpots(parkingSpots);
                    floorService.updateCarParkFloor(floor, floor.getIdentifier());
                }

                List<Floor> floorList = p.getFloors();
                floorList.add(floor);
                p.setFloors(floorList);
                parkingHouseService.updateCarPark(p);
                floorDto = floorFactory.transformToDto(floor);
            } catch (Exception e) {
                return Response.status(400).build();
            }
            return Response.status(201)
                    .entity(json.writeValueAsString(floorDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("/{id}/floors/{identifier}")
    public Response deleteFloor(@PathParam("id") Long id, @PathParam("identifier") String identifier) {
        try {
            ParkingHouse parkingHouse = parkingHouseService.getCarPark(id);
            List<Floor> floorList = parkingHouse.getFloors();
            floorList.removeIf(f -> f.getId().equals(floorService.getByIdentifier(id, identifier).getId()));
            parkingHouse.setFloors(floorList);
            parkingHouseService.updateCarPark(parkingHouse);
            floorService.deleteCarParkFloor(id, identifier);
            return Response.status(204).build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @PUT
    @Path("/{id}/floors/{identifier}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFloor(@PathParam("id") Long id, @PathParam("identifier") String identifier, String body) {
        try {
            Floor floor = json.readValue(body, Floor.class);
            floor.setCarPark(parkingHouseService.getCarPark(id));
            floor = floorService.updateCarParkFloor(floor, identifier);
            FloorDto floorDto = floorFactory.transformToDto(floor);

            return Response.status(200)
                    .entity(json.writeValueAsString(floorDto))
                    .build();
        } catch (Exception e) {
            return Response.status(400).build();
        }
    }

    @GET
    @Path("/{id}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpots(@PathParam("id") Long id, @QueryParam("free") @DefaultValue("") String free) {
        List<SpotDto> spotDtoList = null;
        if (free.isEmpty()) {
            try {
                List<ParkingPlace> parkingPlaces = spotService.getParkingSpots(id);
                spotDtoList = parkingPlaces.stream().map(spotFactory::transformToDto).collect(Collectors.toList());
            } catch (Exception e) {
                return "{}";
            }

        } else {
            try {
                List<ParkingPlace> parkingPlaces = spotService.getParkingSpots(id, free);
                spotDtoList = parkingPlaces.stream().map(spotFactory::transformToDto).collect(Collectors.toList());
            } catch (Exception e) {
                return "{}";
            }
        }
        try {
            return json.writeValueAsString(spotDtoList);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @GET
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpotsById(@PathParam("id") Long id, @PathParam("identifier") String identifier) {
        List<SpotDto> spotDtoList;
        try {
            List<ParkingPlace> parkingPlaces = spotService.getParkingSpotsByIdentifier(id, identifier);
            spotDtoList = parkingPlaces.stream().map(spotFactory::transformToDto).collect(Collectors.toList());
        } catch (Exception e) {
            return "{}";
        }
        try {
            return json.writeValueAsString(spotDtoList);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @POST
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSpot(@PathParam("id") Long id, @PathParam("identifier") String identifier, String body) {
        try {
            ParkingPlace parkingPlace = json.readValue(body, ParkingPlace.class);
            Floor floor;
            try {
                floor = floorService.getByIdentifier(id, identifier);
                parkingPlace.setCarParkFloor(floor);
                parkingPlace.setCarPark(parkingHouseService.getCarPark(id));
                spotService.createParkingSpot(parkingPlace);
            } catch (Exception e) {
                return Response.status(400).build();
            }

            List<ParkingPlace> parkingPlaceList = floor.getSpots();
            parkingPlaceList.add(parkingPlace);
            floorService.updateCarParkFloor(floor, identifier);

            SpotDto spotDto = spotFactory.transformToDto(parkingPlace);
            return Response.status(201)
                    .entity(json.writeValueAsString(spotDto))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }


}
