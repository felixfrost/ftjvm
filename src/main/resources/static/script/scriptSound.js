

     function startMusic() {

        var audio = new Audio("Sound/backgroundMusic.mp3");
        audio.volume = 0.5;
        audio.play();

        }

        function hoverOver() {

        var audio = new Audio("Sound/Plopp.wav")
        audio.volume = 0.7;
        audio.play();


        }

        function hoverOverStop() {

                var audio = new Audio("Sound/Plopp.wav")
                audio.stop();
                audio.currentTime = 0;

                }

        function onClickPlay() {
        var audio = new Audio("Sound/Yeah.wav")
                audio.play();
        }

        function onClickSad() {
        var audio = new Audio("Sound/Wrong.wav")
                audio.play();
        }

        function onClickHappy() {
                var audio = new Audio("Sound/Right.wav")
                        audio.play();
                }