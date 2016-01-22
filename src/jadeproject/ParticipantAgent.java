package jadeproject;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by user on 15-01-2016.
 */
public class ParticipantAgent extends Agent {
    public AID[] contactList;

    private ParticipantGui gui;
    private Calendar calendar;
    private int interval;
    private int dayOfMeeting;

    @Override
    protected void setup() {
        // init variables
        calendar = new Calendar();
        interval = 5000;
        dayOfMeeting = -1; // -1 flag for not requesting meeting

        // init gui
        gui = new ParticipantGui(this);
        gui.display();

        // welcome message
        Konsole.welcome(getAID().getLocalName());
        Konsole.calendar(calendar);

        // get interval search from args
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            interval = Integer.parseInt(args[0].toString());
        }

        // define agent information
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("participant");
        sd.setName("JADE-partcipant");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new TickerBehaviour(this, interval) {
            protected void onTick() {
                //search only if meeting was requested
                if (dayOfMeeting > 0) {
                    //update a list of known schedulers (DF)
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("scheduler");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        contactList = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            contactList[i] = result[i].getName();
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }
                    myAgent.addBehaviour(new RequestMeeting());
                }
            }
        });

        addBehaviour(new ResolveMeeting());
    }

    @Override
    protected void takeDown() {
        //book selling service deregistration at DF
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Participant agent " + getAID().getName() + " terminated.");
    }

    public void requestMeeting(final int value) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                dayOfMeeting = value;
            }
        });
    }

    public boolean isDayAvailable(int day) {
        Day d = calendar.getDaybyNum(day);
        if (d != null) {
            for (TimeSlot t : d.getTimeSlots()) {
                if (t.getPreference() > 0.0)
                    return true;
            }
        }
        return false;
    }

    private class RequestMeeting extends Behaviour {
        private MessageTemplate mt;
        private int step = 0;

        @Override
        public void action() {
            if (step == 0) {
                if (dayOfMeeting > 0) {
                    // requesting meeting
                    Konsole.lookingForMeeting(getAID().getLocalName(), dayOfMeeting);

                    ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                    for (AID a : contactList) {
                        req.addReceiver(a);
                    }
                    req.setContent(Integer.toString(dayOfMeeting));
                    req.setConversationId("meeting-request");
                    req.setReplyWith("request" + System.currentTimeMillis()); //unique value
                    req.setSender(getAID()); // set which participant is sending
                    myAgent.send(req);
                    mt = MessageTemplate.and(MessageTemplate.MatchConversationId("meeting-request"),
                            MessageTemplate.MatchInReplyTo(req.getReplyWith()));

                    dayOfMeeting = -1;
                    step = 1;
                }
            } else if (step == 1) {
                // waiting for meeting proposal
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null) {
                    Konsole.waitingForAction(getAID().getLocalName());

                    if (reply.getPerformative() == ACLMessage.AGREE) {
                        if (reply.getContent().equals("OK"))
                            step = 2;
                    }
                    else if (reply.getPerformative() == ACLMessage.REFUSE) {
                        // day not valid for meeting
                    }
                } else
                    block();
            }
        }

        @Override
        public boolean done() {
            if (step == 2)
                Konsole.meetingRequestDone(getAID().getLocalName());
            return (step == 2);
        }
    }

    private class ResolveMeeting extends CyclicBehaviour {
        private MessageTemplate mt;
        private int day = 0;
        private int timeslot = -1;
        private int step = 0;

        @Override
        public void action() {
            if (step == 0) {
                // Get meeting proposal and accept/refuse to take part
                mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {
                    day = Integer.parseInt(msg.getContent());
                    String scheduler = msg.getSender().getLocalName();

                    ACLMessage reply = msg.createReply();

                    System.out.println(getAID().getLocalName() + ": " + scheduler + " is asking if I can meet on day "
                            + day);

                    if (isDayAvailable(day)) {
                        // check if is a valid day on calendar according to Settings.Class
                        reply.setPerformative(ACLMessage.AGREE);
                        reply.setContent("OK");
                        step = 1;
                    } else {
                        // not a valid day
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("not-available");
                        step = 2;
                    }
                    myAgent.send(reply);
                } else {
                    block();
                }
            } else if (step == 1) {
                // Send calendar preferences
                mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                        MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));

                ACLMessage msg = myAgent.receive(mt);

                if (msg != null) {
                    ACLMessage reply = msg.createReply();

                    if (msg.getPerformative() == ACLMessage.PROPOSE) {
                        timeslot = Integer.parseInt(msg.getContent());

                        if (timeslot >= 0) {
                            double pref = calendar.getDaybyNum(day).getTimeSlots().get(timeslot).getPreference();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent(String.valueOf(pref));
                        } else {
                            reply.setPerformative(ACLMessage.REFUSE);
                            reply.setContent("not-available");
                            step = 2;
                        }

                        myAgent.send(reply);
                    } else if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                        // update calendar with new meeting details
                        String ret[] = msg.getContent().split(";");
                        if (ret != null) {
                            day = Integer.parseInt(ret[0]);
                            timeslot = Integer.parseInt(ret[1]);
                            calendar.getDaybyNum(day).getTimeSlots().get(timeslot).setPreference(0.0);
                            Konsole.finishMeetingDetails(getAID().getLocalName());
                            Konsole.calendar(calendar);
                            step = 0;
                        } else
                            step = 2;
                    }
                } else {
                    block();
                }
            }
        }
    }
}
