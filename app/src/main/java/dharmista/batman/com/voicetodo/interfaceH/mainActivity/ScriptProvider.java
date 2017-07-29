package dharmista.batman.com.voicetodo.interfaceH.mainActivity;

/**
 * Created by Dharmista on 7/20/2017.
 */

public class ScriptProvider {

    private String script = "\n" +
            "    class Stopwatch {\n" +
            "        constructor(display, results) {\n" +
            "            this.running = false;\n" +
            "            this.display = display;\n" +
            "            this.results = results;\n" +
            "            this.laps = [];\n" +
            "            this.breakPoint = false;\n" +
            "            this.target = null;\n" +
            "            this.reset();\n" +
            "            this.print(this.times);\n" +
            "        }\n" +
            "\n" +
            "        reset() {\n" +
            "            this.times = [ 0, 0, 0 ];\n" +
            "            this.print();\n" +
            "        }\n" +
            "\n" +
            "        start() {\n" +
            "            if (!this.time) this.time = performance.now();\n" +
            "            if (!this.running) {\n" +
            "                this.running = true;\n" +
            "                requestAnimationFrame(this.step.bind(this));\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        stop() {\n" +
            "            this.running = false;\n" +
            "            this.time = null;\n" +
            "        }\n" +
            "\n" +
            "        restart() {\n" +
            "            if (!this.time) this.time = performance.now();\n" +
            "            if (!this.running) {\n" +
            "                this.running = true;\n" +
            "                requestAnimationFrame(this.step.bind(this));\n" +
            "            }\n" +
            "            this.reset();\n" +
            "        }\n" +
            "\n" +
            "        playTill(a,b,c){\n" +
            "            this.breakPoint = true;\n" +
            "            this.target = [a, b, c];\n" +
            "            if (!this.time) this.time = performance.now();\n" +
            "            if (!this.running) {\n" +
            "                this.running = true;\n" +
            "                requestAnimationFrame(this.step.bind(this));\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        step(timestamp) {\n" +
            "            if (!this.running) return;\n" +
            "            this.calculate(timestamp);\n" +
            "            this.time = timestamp;\n" +
            "            this.print();\n" +
            "            if(this.breakPoint){\n" +
            "                if(parseInt((this.target[0]+\"\").substring(0,2))==parseInt((this.times[0]+\"\").substring(0,2))) {\n" +
            "                    {\n" +
            "                        if (parseInt((this.target[1] + \"\").substring(0, 2)) == parseInt((this.times[1] + \"\").substring(0, 2))) {\n" +
            "                            if (parseInt((this.target[2] + \"\").substring(0, 2)) < parseInt((this.times[2] + \"\").substring(0, 2))) {\n" +
            "                                this.stop();\nclearInterval(rippleInstance);document.getElementById(\"recordButton\").innerHTML = \"<img class='icon' src='play.png'/>\";\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "            requestAnimationFrame(this.step.bind(this));\n" +
            "        }\n" +
            "\n" +
            "        calculate(timestamp) {\n" +
            "            const diff = timestamp - this.time;\n" +
            "            this.times[2] += diff / 10;\n" +
            "            if (this.times[2] >= 100) {\n" +
            "                this.times[1] += 1;\n" +
            "                this.times[2] -= 100;\n" +
            "            }\n" +
            "            if (this.times[1] >= 60) {\n" +
            "                this.times[0] += 1;\n" +
            "                this.times[1] -= 60;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        print() {\n" +
            "            this.display.innerText = this.format(this.times);\n" +
            "        }\n" +
            "\n" +
            "        format(times) {\n" +
            "            return pad0(times[0], 2)+\":\"+pad0(times[1], 2)+\":\"+pad0(Math.floor(times[2]), 2);    }\n" +
            "    }\n" +
            "\n" +
            "    function pad0(value, count) {\n" +
            "        let result = value.toString();\n" +
            "        for (; result.length < count; --count)\n" +
            "            result = '0' + result;\n" +
            "        return result;\n" +
            "    }\n" +
            "\n" +
            "    function clearChildren(node) {\n" +
            "        while (node.lastChild)\n" +
            "            node.removeChild(node.lastChild);\n" +
            "    }\n" +
            "    let stopwatch = new Stopwatch(\n" +
            "        document.querySelector('.stopwatch'),\n" +
            "        document.querySelector('.results'));\n" +
            "\n" +
            "    let recordng = false, playing = false;\n" +
            "\n" +
            "    let numberOfRetrails = 0, rippleInstance;\n" +
            "    let colorCodeSpreadAmount = 5;\n" +
            "\n" +
            "    const a = function ripple() {\n" +
            "        if (colorCodeSpreadAmount >= 20) {\n" +
            "            colorCodeSpreadAmount = 5;\n" +
            "        }\n" +
            "        let div = document.getElementById(\"recordButton\").style;\n" +
            "        div.boxShadow = \"0 0 20px \"+colorCodeSpreadAmount+\"px rgb(226, 73, 222)\";\n" +
            "        colorCodeSpreadAmount += 5;\n" +
            "    };\n" +
            "\n" +
            "    let completedRecordig = false, voiceTime = \"\";\n" +
            "    document.getElementById(\"recordButton\").onclick = function handleRecording(){\n" +
            "        if(recordng && !playing) {\n" +
            "            stopwatch.stop();\n" +
            "            if(completedRecordig) {\n" +
            "                voiceTime = Android.stopRecordingVoice();\n" +
            "                let dutes = voiceTime.split('|');\n" +
            "                stopwatch.times = [parseInt(pad0(dutes[0],2)),parseInt(pad0(dutes[1],2)),parseInt(pad0(dutes[2],2))];\n" +
            "                stopwatch.print();\n" +
            "            }\n" +
            "            else\n" +
            "                Android.stopPlaying();\n" +
            "            clearInterval(rippleInstance);\n" +
            "            document.getElementById(\"recordButton\").innerHTML = \"<img class='icon' src='play.png'/>\";\n" +
            "            recordng = false;\n" +
            "            playing = true;\n" +
            "        }\n" +
            "        else if (!recordng && playing) {\n" +
            "            stopwatch.reset();" +
            "            Android.playLastRecorded();\n" +
            "            let dutes = voiceTime.split('|');"+
            "            stopwatch.playTill(parseInt(pad0(dutes[0],2)),parseInt(pad0(dutes[1],2)),parseInt(pad0(dutes[2],2)));\n" +
            "            clearInterval(rippleInstance); rippleInstance = setInterval(a, 150);\n" +
            "            completedRecordig = false;\n" +
            "            document.getElementById(\"recordButton\").innerHTML = \"<img class='icon' src='record.png'/>\";\n" +
            "            playing = false;\n" +
            "            recordng = true;\n" +
            "        }\n" +
            "        else if(!recordng && !playing) {\n" +
            "            stopwatch.start();\n" +
            "            Android.recordVoice();\n" +
            "            completedRecordig = true;\n" +
            "            rippleInstance = setInterval(a, 150);\n" +
            "            document.getElementById(\"recordButton\").innerHTML = \"<img class='icon' src='record.png'/>\";\n" +
            "            recordng = true;\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    document.getElementById(\"reset_button\").onclick = function reset_every_thing() {\n" +
            "        if(!document.getElementById(\"reset_button\").classList.contains(\"disabled\")) {\n" +
            "            Android.reset_refresh();\n" +
            "        }\n" +
            "    };\n" +
            "\n" +
            "    document.getElementById(\"timeAndDate\").onclick = function setDateAndTime() {\n" +
            "        Android.setAlarm();\n" +
            "    }";

    public String getScript(String script) {
        return "<script>" + script + "</script>";
    }

    public String getFrontScript() {
        return getScript(script);
    }
}
