package Sheep_BC.Main;

import Sheep_BC.Commands.Basic;
import Sheep_BC.Annotations.*;
import Sheep_BC.Commands.Recommand;
import Sheep_BC.Commands.Sing;
import Sheep_BC.Commands.Users.User;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

public class MessageReceived implements Runnable{

    MessageReceivedEvent event;
    String[] args;
    MessageChannel channel;

    String[] prefixes = {"숩","ㅅ","t"};
    public static Class[] event_class = new Class[]{
            Basic.class,
            Recommand.class,
            Sing.class,
            User.class
    };

    public MessageReceived(MessageReceivedEvent event){
        this.event = event;
        args = event.getMessage().getContentRaw().split(" ");
        channel = event.getChannel();
    }

    @Override
    public void run() {
      if(!check_prefix(args[0])) return;

      Class clas = getClass(remove_prefix(args[0]));
      if(clas == null) return;
      runMethod(clas,remove_prefix(args[0]));

    }

    private Class getClass(String command){
        for(Class clas: event_class){
            if(find_Method(clas.getMethods(),command) != null) return clas;
        }
        return null;
    }

    private void runMethod(Class clas, String command){
        Object[] class_input = {event,args};
        Class[] class_input_types = {MessageReceivedEvent.class,String[].class};

        try {

            Constructor constructor = clas.getConstructor(class_input_types);
            Object instance = constructor.newInstance(class_input);
            clas = instance.getClass();

            Method method = find_Method(clas.getMethods(),command);
            method.invoke(instance);

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    private Method find_Method(Method[] list, String command){
        for(Method method: list){
            Annotation[] annotations = method.getAnnotations();
            for(Annotation annotation: annotations){
                if(annotation instanceof MessageEvent){
                    MessageEvent messageEvent = (MessageEvent) annotation;
                    if(changeToArray(messageEvent.key()).contains(command.toUpperCase(Locale.ROOT))){
                        return method;
                    }
                }
            }
        }
        return null;
    }

    private boolean check_prefix(String command){
        for(String prefix: prefixes){
            if(command.startsWith(prefix)) return true;
        }
        return false;
    }

    private String remove_prefix(String command){
        for(String prefix: prefixes){
            if(command.startsWith(prefix)) return command.substring(prefix.length());
        }
        return command;
    }

    private ArrayList<String> changeToArray(String[] args){
        ArrayList<String> list = new ArrayList<>();
        for(String arg: args){
            list.add(arg.toUpperCase(Locale.ROOT));
        }
        return list;
    }
}
