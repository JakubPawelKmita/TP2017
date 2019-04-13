package server;

import org.junit.Before;
import org.junit.Test;
import server.messages.MessagePayloadValidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessagePayloadValidatorTest {
    private MessagePayloadValidator messagePayloadValidator;

    @Before
    public void setUp(){
        messagePayloadValidator = new MessagePayloadValidator();
    }
    @Test
    public void testRequestWithValidPayloadHandShake() {
        String validPayload = "{\"action\": \"HANDSHAKE\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";

        assertTrue(messagePayloadValidator.isRequestValid(validPayload));

    }

    @Test
    public void testRequestWithValidPayloadCreateRoom() {
        String validPayload = "{\"action\": \"CREATE_ROOM\",\"client_name\": \"olaf\",\"payload\": \"writing unit tests\"}";

        assertTrue(messagePayloadValidator.isRequestValid(validPayload));

    }

    @Test
    public void testRequestWithValidPayloadJoinRoom() {
        String validPayload = "{\"action\": \"join_room\",\"client_name\": \"olaf\",\"payload\": \"room nb 3\"}";

        assertTrue(messagePayloadValidator.isRequestValid(validPayload));

    }

    @Test
    public void testRequestWithValidPayloadGetRooms() {
        String validPayload = "{\"action\": \"GET_ROOMS\",\"client_name\": \"olaf\",\"payload\": \"\"}";

        assertTrue(messagePayloadValidator.isRequestValid(validPayload));

    }

    @Test
    public void testRequestWithInvalidActionKey() {
        String invalidPayload = "{\"an\": \"HANDSHAKE\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";

        assertFalse(messagePayloadValidator.isRequestValid(invalidPayload));

    }

    @Test
    public void testRequestWithInvalidClientNameKey() {
        String invalidPayload = "{\"action\": \"HANDSHAKE\",\"client_shoe_size\": \"some_name\",\"payload\": \"empty_payload\"}";

        assertFalse(messagePayloadValidator.isRequestValid(invalidPayload));

    }

    @Test
    public void testRequestWithInvalidPayloadKey() {
        String invalidPayload = "{\"action\": \"HANDSHAKE\",\"client_shoe_size\": \"some_name\",\"deep_load\": \"empty_payload\"}";

        assertFalse(messagePayloadValidator.isRequestValid(invalidPayload));

    }

    @Test
    public void testRequestWithInvalidActionName() {
        String invalidPayload = "{\"action\": \"handFAKE\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";

        assertFalse(messagePayloadValidator.isRequestValid(invalidPayload));

    }

    @Test
    public void testRequestWithValidActionNameInLowercase() {
        String invalidPayload = "{\"action\": \"handshake\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";

        assertTrue(messagePayloadValidator.isRequestValid(invalidPayload));

    }

    @Test
    public void testRequestWithValidActionNameInLowercase2() {
        String invalidPayload = "{\"action\": \"get_rooms\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";

        assertTrue(messagePayloadValidator.isRequestValid(invalidPayload));

    }

}