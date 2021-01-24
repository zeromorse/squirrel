package io.github.zeromorse.fsm.context.config.marker.impl;

import com.sankuai.meituan.waimai.config.fsm.agent.context.config.marker.ScxmlMarker;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.CoreErrorCodes;
import com.sankuai.meituan.waimai.config.fsm.agent.exception.FsmAgentRuntimeException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 将SCXML加入SQRL标记
 */
public final class JDomScxmlMarker implements ScxmlMarker {
    private final static Logger logger = LoggerFactory.getLogger(JDomScxmlMarker.class);
    private static final String stateElementName = "state";
    private static final String transitionElementName = "transition";

    SqrlMark sqrlMark = SqrlMark.DEFAULT;
    MarkConstantProvider markConstantProvider = MarkConstantProvider.DEFAULT;

    public JDomScxmlMarker() {
    }

    public void setSqrlMark(SqrlMark sqrlMark) {
        this.sqrlMark = sqrlMark;
    }

    public void setMarkConstantProvider(MarkConstantProvider markConstantProvider) {
        this.markConstantProvider = markConstantProvider;
    }

    /**
     * 装饰标准 SCXML，加入特定的 sqrl 标记
     */
    public String decorate(String scxml) {
        return decorate(new ByteArrayInputStream(scxml.getBytes()));
    }

    /**
     * 装饰标准 SCXML，加入特定的 sqrl 标记
     */
    public String decorate(InputStream source) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(source);
            Element scxml = document.getRootElement();
            Candidate candidate = parseCandidate(scxml);
            addSqrlTag(scxml, candidate);
            return toXmlString(document).replace(("\n"), "").replace("\r", "");
        } catch (ExcessException e) {
            logger.error("Stop decoration for {}, please check source", e.getMessage());
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.ELEMENT_EXCESS_ERROR, e.elementName, e.bound);
        } catch (JDOMException | IOException e) {
            logger.error("Stop parse for {}, please check source", e.getMessage());
            throw new FsmAgentRuntimeException(e, CoreErrorCodes.PARSE_XML_ERROR, source);
        }
    }

    /**
     * 加入 Squirrel-foundation 中特有的标签
     */
    void addSqrlTag(Element scxml, Candidate candidate) {
        // 加入 sqrl 的命名空间
        scxml.addNamespaceDeclaration(sqrlMark.sqrlNamespace);
        // 加入 sqrl:fsm 元素
        scxml.addContent(0, sqrlMark.sqrlFsmElement());

        // 对 candidate.states 补充 onentry、onexit 节点
        for (Element state : candidate.states) {
            state.addContent(sqrlMark.onentryElement());
            state.addContent(sqrlMark.onexitElement());
        }
        // 对 candidate.transitions 丰富 transition 节点
        List<Element> transitions = candidate.transitions;
        for (Element transition : transitions) {
            transition.getAttributes().addAll(sqrlMark.transitionAttrs());
            transition.addContent(sqrlMark.transitionActionElement());
        }
    }

    /**
     * 从 [scxml] 节点中解析出所有需要修改的候选节点
     */
    Candidate parseCandidate(Element scxml) throws ExcessException {
        Candidate candidate = new Candidate();
        candidate.fillUp(scxml);
        return candidate;
    }

    /**
     * 转化为 XML 字符串
     */
    String toXmlString(Document document) {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getCompactFormat());
        return xmlOutput.outputString(document);
    }

    /**
     * 过量异常，用于中断递归处理
     */
    final static class ExcessException extends Exception {
        String elementName;
        int bound;

        private ExcessException(String elementName, int bound) {
            super(elementName + "excess, limit " + bound);
            this.elementName = elementName;
            this.bound = bound;
        }
    }

    /**
     * 待处理的节点，包含 state 和 transition 节点
     */
    class Candidate {
        final List<Element> states = new ArrayList<>();
        final List<Element> transitions = new ArrayList<>();

        /**
         * 追加 state 节点
         */
        void appendState(Element state) throws ExcessException {
            int stateMaxSize = markConstantProvider.getStateMaxSize();
            if (states.size() > stateMaxSize) {
                throw new ExcessException(stateElementName, stateMaxSize);
            }
            states.add(state);
        }

        /**
         * 追加 transition 节点
         */
        void appendTransition(Element transition) throws ExcessException {
            int transitionMaxSize = markConstantProvider.getTransitionMaxSize();
            if (transitions.size() > transitionMaxSize) {
                throw new ExcessException(transitionElementName, transitionMaxSize);
            }
            transitions.add(transition);
        }

        /**
         * 递归地使用 [root] 中的元素填充
         */
        private void fillUp(Element root) throws ExcessException {
            List<Element> children = root.getChildren();
            if (children == null || children.isEmpty()) {
                return;
            }

            for (Element child : children) {
                switch (child.getName()) {
                    case stateElementName:
                        appendState(child);
                        break;
                    case transitionElementName:
                        appendTransition(child);
                        break;
                }

                fillUp(child);
            }
        }
    }
}
