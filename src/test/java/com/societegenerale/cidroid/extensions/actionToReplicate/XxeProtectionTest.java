package com.societegenerale.cidroid.extensions.actionToReplicate;

import com.societegenerale.cidroid.api.IssueProvidingContentException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class XxeProtectionTest {

    private final AddXmlContentAction action = new AddXmlContentAction();

    private final String pomWithExternalEntity = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE project [ <!ENTITY xxe SYSTEM \"file:///etc/passwd\"> ]>\n" +
            "<project>\n" +
            "\t<build>&xxe;</build>\n" +
            "</project>";

    @Test
    public void shouldNotResolveExternalEntitiesInDocumentToProcess() {

        action.setXpathUnderWhichElementNeedsToBeAdded("//build");
        action.setElementToAdd("<someElement>hello</someElement>");

        assertThatThrownBy(() -> action.provideContent(pomWithExternalEntity))
                .isInstanceOf(IssueProvidingContentException.class);
    }

    @Test
    public void shouldNotResolveExternalEntitiesInElementToAdd() {

        action.setXpathUnderWhichElementNeedsToBeAdded("//build");
        action.setElementToAdd(pomWithExternalEntity);

        assertThatThrownBy(() -> action.provideContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?><project><build></build></project>"))
                .isInstanceOf(IssueProvidingContentException.class);
    }
}
