/*
 * File:    $HeadURL: https://svn.code.sf.net/p/jvoicexml/code/trunk/org.jvoicexml/unittests/src/org/jvoicexml/interpreter/tagstrategy/TagStrategyTestBase.java $
 * Version: $LastChangedRevision: 4318 $
 * Date:    $Date: 2014-10-16 13:32:35 +0200 (Thu, 16 Oct 2014) $
 * Author:  $LastChangedBy: schnelle $
 *
 * JVoiceXML - A free VoiceXML implementation.
 *
 * Copyright (C) 2007-2014 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.profile.vxml21.tagstrategy;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.jvoicexml.ImplementationPlatform;
import org.jvoicexml.Session;
import org.jvoicexml.event.JVoiceXMLEvent;
import org.jvoicexml.implementation.SynthesizedOutputListener;
import org.jvoicexml.interpreter.Dialog;
import org.jvoicexml.interpreter.FormInterpretationAlgorithm;
import org.jvoicexml.interpreter.VoiceXmlInterpreter;
import org.jvoicexml.interpreter.VoiceXmlInterpreterContext;
import org.jvoicexml.interpreter.datamodel.DataModel;
import org.jvoicexml.interpreter.datamodel.DataModelObjectSerializer;
import org.jvoicexml.interpreter.dialog.ExecutablePlainForm;
import org.jvoicexml.mock.implementation.MockImplementationPlatform;
import org.jvoicexml.profile.Profile;
import org.jvoicexml.profile.TagStrategy;
import org.jvoicexml.profile.TagStrategyFactory;
import org.jvoicexml.xml.VoiceXmlNode;
import org.jvoicexml.xml.vxml.Block;
import org.jvoicexml.xml.vxml.Form;
import org.jvoicexml.xml.vxml.VoiceXmlDocument;
import org.jvoicexml.xml.vxml.Vxml;
import org.mockito.Mockito;

/**
 * Base class for tests of {@link org.jvoicexml.profile.TagStrategy}.
 *
 * @author Dirk Schnelle-Walka
 * @version $Revision: 4318 $
 * @since 0.6
 */
public abstract class TagStrategyTestBase {
    /** The implementation platform. */
    private MockImplementationPlatform platform;

    /** The VoiceXML interpreter context. */
    private VoiceXmlInterpreterContext context;

    /** The VoiceXML interpreter. */
    private VoiceXmlInterpreter interpreter;

    /** The fia. */
    private FormInterpretationAlgorithm fia;

    /** The profile. */
    private Profile profile;

    /** The employed data model */
    private DataModel model;

    /**
     * Constructs a new object.
     */
    public TagStrategyTestBase() {
        super();
    }

    /**
     * Retrieves the data model
     * 
     * @return the data mdoel
     */
    protected final DataModel getDataModel() {
        return model;
    }

    /**
     * Retrieves the interpreter.
     * 
     * @return the interpreter.
     */
    protected final VoiceXmlInterpreter getInterpreter() {
        return interpreter;
    }

    /**
     * Retrieves the {@link VoiceXmlInterpreterContext}.
     * 
     * @return the voice XML interpreter context.
     */
    protected final VoiceXmlInterpreterContext getContext() {
        return context;
    }

    /**
     * Retrieves the {@link ImplementationPlatform}.
     * 
     * @return the implementation platform
     */
    protected final ImplementationPlatform getImplementationPlatform() {
        return platform;
    }

    /**
     * {@inheritDoc}
     */
    @Before
    public final void baseSetUp() throws Exception {
        platform = new MockImplementationPlatform();
        profile = Mockito.mock(Profile.class);
        final TagStrategyFactory taginitfactory = Mockito
                .mock(TagStrategyFactory.class);
        Mockito.when(profile.getInitializationTagStrategyFactory()).thenReturn(
                taginitfactory);
        context = Mockito.mock(VoiceXmlInterpreterContext.class);
        Mockito.when(context.getProfile()).thenReturn(profile);
        final Session session = Mockito.mock(Session.class);
        Mockito.when(context.getSession()).thenReturn(session);
        interpreter = new VoiceXmlInterpreter(context);
        model = Mockito.mock(DataModel.class);
        final DataModelObjectSerializer serializer = Mockito
                .mock(DataModelObjectSerializer.class);
        Mockito.when(model.getSerializer()).thenReturn(serializer);
        Mockito.when(context.getDataModel()).thenReturn(model);
    }

    /**
     * Sets the output listener to add once the system output is obtained.
     * 
     * @param listener
     *            the listener.
     */
    public final void setSystemOutputListener(
            final SynthesizedOutputListener listener) {
        platform.setSystemOutputListener(listener);
    }

    /**
     * Convenience method to creates an empty form.
     * 
     * @return the created form.
     */
    protected final Form createForm() {
        final VoiceXmlDocument document = createDocument();
        final Vxml vxml = document.getVxml();
        return vxml.appendChild(Form.class);
    }

    /**
     * Convenient method to create a new VoiceXML document.
     * 
     * @return the newly created VoiceXML document.
     */
    protected final VoiceXmlDocument createDocument() {
        fia = null;

        final VoiceXmlDocument document;

        try {
            document = new VoiceXmlDocument();
        } catch (ParserConfigurationException pce) {
            Assert.fail(pce.getMessage());

            return null;
        }
        return document;
    }

    /**
     * Creates a <code>&lt;block&gt;</code> inside the VoiceXML document.
     *
     * @return Created block.
     */
    protected final Block createBlock() {
        return createBlock(null);
    }

    /**
     * Creates a <code>&lt;block&gt;</code> inside the VoiceXML document.
     *
     * @param doc
     *            Optional VoiceXML document.
     *
     * @return Created block.
     * @exception Exception
     *                error creating the FIA
     */
    protected final Block createBlock(final VoiceXmlDocument doc) {
        final VoiceXmlDocument document;

        if (doc == null) {
            document = createDocument();
        } else {
            document = doc;
        }

        final Vxml vxml = document.getVxml();
        final Form form = vxml.appendChild(Form.class);
        createFia(form);

        return form.appendChild(Block.class);
    }

    /**
     * Creates a fia for the given form.
     * 
     * @param form
     *            the form for which to create a fia.
     */
    protected final void createFia(final Form form) {
        final Dialog executableForm = new ExecutablePlainForm();
        executableForm.setNode(form);
        fia = new FormInterpretationAlgorithm(context, interpreter,
                executableForm);
    }

    /**
     * Convenient method to execute the tag strategy.
     * 
     * @param node
     *            the node.
     * @param strategy
     *            the tag strategy.
     * @exception JVoiceXMLEvent
     *                error executing the strategy.
     * @exception Exception
     *                error executing the strategy.
     */
    protected final void executeTagStrategy(final VoiceXmlNode node,
            final TagStrategy strategy) throws JVoiceXMLEvent, Exception {
        if (fia != null) {
            fia.initialize(profile);
        }
        strategy.getAttributes(context, fia, node);
        strategy.evalAttributes(context);
        strategy.validateAttributes(model);
        strategy.execute(context, interpreter, fia, null, node);
    }
}
