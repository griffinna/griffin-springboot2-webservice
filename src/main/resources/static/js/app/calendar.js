var calendar = {
    init: function () {
        var cal = new tui.Calendar('#calendar', {
            defaultView: 'month' // monthly view option
        });
        console.log(cal);
    }
}

calendar.init();