package ports.input.service;

//command handler

import dto.create.CreateOrderCommand;
import dto.create.CreateOrderResponse;
import dto.track.TrackOrderQuery;
import dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse createTrack(@Valid TrackOrderQuery trackOrderQuery);
}
