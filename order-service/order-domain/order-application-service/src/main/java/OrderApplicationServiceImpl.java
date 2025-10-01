import dto.create.CreateOrderCommand;
import dto.create.CreateOrderResponse;
import dto.track.TrackOrderQuery;
import dto.track.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ports.input.service.OrderApplicationService;

@Service
@Validated
@Slf4j
public class OrderApplicationServiceImpl implements OrderApplicationService {
    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return null;
    }

    @Override
    public TrackOrderResponse createTrack(TrackOrderQuery trackOrderQuery) {
        return null;
    }
}
