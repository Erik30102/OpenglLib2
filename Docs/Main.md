# Window Class
---
Um ein window zu erstellen Muss mann einfach eine neue Instance von Window erstellen(paramter: width, height, title, EventCallbackFunction). Um Diese dann auch Benutzen zu können und Das Gerenderte auf dem Display zu Zeigen muss jeden Frame die Methode ``window.OnUpdate();`` aufgerufen werden.

Um automatisch sachen wie schlißen des Fensters zu handeln kann man die funktion: ``window.ShouldClose()`` in dem haupt game loop

Bsp.:
```java
Window window = new Window(600, 380, "Test", event -> {});

while(!window.ShouldClose()){
    window.OnUpdate();
}
```

# Events
---
Die Event Callback function ist eine methode die so aufgebaut ist: ``public void OnEvent(Event event)`` diese immt als input die abstrakte klasse Event nachdem man diese mit der Methode ``GetEventType()`` Identifiziert hat kann man den typen vergleichen und dann zu der Klasse Casten und Nutzen

Bsp.:
```java
...
Window window = new Window(600, 380, "Test", this.OnEvent);
...

public void OnEvent(Event event) {
    if(event.GetEventType() == EventType.WindowResize){
        System.out.println("Wow das Window ist jetzt: " + ((WindowResizeEvent)event).GetWidth() + "breit"); 
    }
}
```

für eine liste der verschiedenen Events schaut einfach den source code an in OpenGlLib/src/main/java/org/openglLib/Window/Events name und auch source code ist javadocs erklärt

# Wie Bekomm ich was auf den Screen