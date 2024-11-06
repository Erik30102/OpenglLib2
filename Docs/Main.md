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

Zuerst muss jeden frame Der Interne Buffer auf den alles gezeichnet wird gecleard werden sowohl als auch der buffer der die Zeichenreinfolge aufzeichnet ``RenderApi.ClearScreen(); RenderApi.ClearScreenColor(0.4f, 0.2f, 0.1f);`` Die ClearScreenColor funktion überschreibt dann den screen einfach wieder in einer farbe

Um Sachen zu zeichnen kann man es entwerder einfach oder für komplexere dinge schwerer machen:

## Einfach: Spritbaches
Ein Sprite batch 


## Schwer: Custom VAO VBO IBO
wenn man komplexere objecte wie 3d models haben möchte muss man die sachen auf dem Gpu selber schreiben.
1. Schritt: Memory Layout erstellen
>man muss bei eigenen VAOs die reinfolge der daten auf dem Gpu selber bestimmen das macht man indem man ein buffer layout erstellt Bsp.:
```java
BufferLayout layout = new BufferLayout(new BufferElement[] {
    new BufferElement(DataType.VEC3), // POS
    new BufferElement(DataType.VEC2), // UV
});
```
> das sagt das man wenn man jetzt n verticies in nem array dann später da hochladen will würde es so aussehen ``float[] verex = new float[]{Pos1X, Pos1Y, Pos1Z, UV1X, UX1Y, ... POSNX, POSNY, POSNZ, UVNX, UVNY} `` die infos kann man dann später im shader abfragen wo sie dann verarbeite werden hier würde es dann beispielweise so aussehen ``layout(location = 0) in vec3 pos; layout(location = 1) in vec2 uvs;``

2. Schritt: erstellung des VBOs
>jetzt muss man die vertex infos auf den gpu laden und das layout auswählen und zusätzlich entscheiden ob das vbo Dynamic oder static ist bedeutet ob die vertex info später nach dem ersten erstellen sich wieder verändern wird. Bsp:
```java
vbo = new VertexBuffer(this.vertices, BUFFER_USAGE.STATIC_DRAW);
vbo.SetLayout(layout);
```

3. Schritt: die Incies erstellen
>Jetzt muss man die reinfolge angeben in der die vertecies verarbeitet werden und wie sie zusammenhängen da es immer 3 vertex paare seinen müssen die dann ein dreieck ergeben. Also als beispiel für ein vierech wäre es wenn die vertecies so aufgebaut sind (Oben Links 0, Oben Rechs 1, Unten Links 2, Unten Rechts 3) die indecies so: 0, 1, 2, 2, 3, 1 also 2 dreiecke die ein viereck ergeben.
```java
IndexBuffer ibo = new IndexBuffer(new int[]{0, 1, 2, 2, 3, 1});
```

4. Schritt: Das VAO erstellen und mit den anderen vereinen
>Jetzt erstllt man das VAO was eigentlich nur alles vereint und es einfacher macht sachen zu rendern ist glaub am einfachsten erklärt wenn man einfach das beispiel sieht
```java
VertexArray vao = new VertexArray();
vao.AddIndexBuffer(ibo);
vao.AddVertexBuffer(vbo);
```

### Jetzt zum rendern dieses VAOs
Um ein object zu rendern braucht man zuerst ein shader wie man diesen erstellt und benutzt wird jetzt hier vorrausgesetzt. man binded zuerste den shader und alle infos die damit verbunden sind binded dann das VAO und Called die Funktion ``RenderApi.DrawStatic(vao)`` und es sollte auf den screen erscheien.