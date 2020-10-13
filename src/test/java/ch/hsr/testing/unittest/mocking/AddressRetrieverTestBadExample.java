package ch.hsr.testing.unittest.mocking;

import ch.hsr.testing.unittest.testbuilderpattern.Address;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class AddressRetrieverTestBadExample {

    private AddressRetriever addressRetriever;

    @Test
    public void testUsingRealOnlineHttpService() throws AddressRetrieverException {
        addressRetriever = new AddressRetriever("FZWYaXm5WUA7TNcUzAflX1wlqKWMcrep");
        Address address = addressRetriever.retrieve(47.4999158,8.7235591);

        Assertions.assertThat(address.getCity()).isEqualTo("Winterthur");
    }

}