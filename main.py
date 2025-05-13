from kivy.app import App
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.button import Button
from kivy.uix.image import Image
from kivy.core.audio import SoundLoader
from kivy.core.window import Window

class MainLayout(FloatLayout):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)

        self.background = Image(source='background.jpg', allow_stretch=True, keep_ratio=True, size_hint=(1, 1), opacity=0)
        self.add_widget(self.background)

        self.sound = SoundLoader.load('sound.wav')

        btn_sound = Button(text='Play Sound', size_hint=(0.3, 0.15), pos_hint={'x':0.05, 'y':0.75})
        btn_sound.bind(on_press=self.play_sound)
        self.add_widget(btn_sound)

        self.btn_toggle = Button(text='Toggle Background', size_hint=(0.3, 0.15), pos_hint={'x':0.35, 'y':0.75})
        self.btn_toggle.bind(on_press=self.toggle_background)
        self.add_widget(self.btn_toggle)

        btn_exit = Button(text='Exit App', size_hint=(0.3, 0.15), pos_hint={'x':0.65, 'y':0.75})
        btn_exit.bind(on_press=self.exit_app)
        self.add_widget(btn_exit)

    def play_sound(self, instance):
        if self.sound:
            self.sound.play()

    def toggle_background(self, instance):
        if self.background.opacity == 0:
            self.background.opacity = 1
            self.btn_toggle.state = 'down'
        else:
            self.background.opacity = 0
            self.btn_toggle.state = 'normal'

    def exit_app(self, instance):
        App.get_running_app().stop()

class TestApp(App):
    def build(self):
        return MainLayout()

if __name__ == '__main__':
    TestApp().run()
