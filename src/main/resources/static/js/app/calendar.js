(function (window, Calendar) {
    var cal, resizeThrottled;
    var useCreationPopup = true;
    var useDetailPopup = true;
    var datePicker, selectedCalendar;

    cal = new Calendar('#calendar', {
        defaultView: 'month',
        useCreationPopup: useCreationPopup,
        useDetailPopup: useDetailPopup,
        calendars: CalendarList,
        template: templates
    });

    // event
    cal.on({
        'clickMore': function (e) {
            console.log('clickMore', e);
        },
        'clickSchedule': function (e) {
            console.log('clickSchedule', e);
        },
        'clickDayname': function (e) {
            console.log('clickDayname', date);
            saveNewSchedule(e);
        }
    })

    function saveNewSchedule(scheduleDate) {
        var calendar = scheduleDate.calendar || findCalendar(scheduleDate.calendarId);
        var schedule = {
            id: String(chance.guid()),
            title: scheduleDate.title,
            isAllDay: scheduleDate.isAllDay,
            start: scheduleDate.start,
            end: scheduleDate.end,
            category: scheduleDate.isAllDay ? 'allday' : 'time',
            dueDateClass: '',
            color: calendar.color,
            bgColor: calendar.bgColor,
            dragBgColor: calendar.bgColor,
            borderColor: calendar.borderColor,
            location: scheduleDate.location,
            raw: {
                class: scheduleDate.raw['class']
            },
            state: scheduleDate.state
        };
        if (calendar) {
            schedule.calendarId = calendar.id;
            schedule.color = calendar.color;
            schedule.bgColor = calendar.bgColor;
            schedule.borderColor = calendar.borderColor;
        }

        cal.createSchedules([schedule]);

        refreshScheduleVisibility();
    }

});


function getTimeTemplate(schedule, isAllDay) {
    var html = [];

    if (!isAllDay) {
        html.push('<strong>' + moment(schedule.start.getTime()).format('HH:mm') + '</strong> ');
    }
    if (schedule.isPrivate) {
        html.push('<span class="calendar-font-icon ic-lock-b"></span>');
        html.push(' Private');
    } else {
        if (schedule.isReadOnly) {
            html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
        } else if (schedule.recurrenceRule) {
            html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
        } else if (schedule.attendees.length) {
            html.push('<span class="calendar-font-icon ic-user-b"></span>');
        } else if (schedule.location) {
            html.push('<span class="calendar-font-icon ic-location-b"></span>');
        }
        html.push(' ' + schedule.title);
    }

    return html.join('');
}

function getGridTitleTemplate(type) {
    var title = '';

    switch(type) {
        case 'milestone':
            title = '<span class="tui-full-calendar-left-content">MILESTONE</span>';
            break;
        case 'task':
            title = '<span class="tui-full-calendar-left-content">TASK</span>';
            break;
        case 'allday':
            title = '<span class="tui-full-calendar-left-content">ALL DAY</span>';
            break;
    }

    return title;
}

function getGridCategoryTemplate(category, schedule) {
    var tpl;

    switch(category) {
        case 'milestone':
            tpl = '<span class="calendar-font-icon ic-milestone-b"></span> <span style="background-color: ' + schedule.bgColor + '">' + schedule.title + '</span>';
            break;
        case 'task':
            tpl = '#' + schedule.title;
            break;
        case 'allday':
            tpl = getTimeTemplate(schedule, true);
            break;
    }

    return tpl;
}

// register templates
var templates = {
    milestone: function(schedule) {
        return getGridCategoryTemplate('milestone', schedule);
    },
    milestoneTitle: function() {
        return getGridTitleTemplate('milestone');
    },
    task: function(schedule) {
        return getGridCategoryTemplate('task', schedule);
    },
    taskTitle: function() {
        return getGridTitleTemplate('task');
    },
    allday: function(schedule) {
        return getTimeTemplate(schedule, true);
    },
    alldayTitle: function() {
        return getGridTitleTemplate('allday');
    },
    time: function(schedule) {
        return getTimeTemplate(schedule, false);
    },
    goingDuration: function(schedule) {
        return '<span class="calendar-icon ic-travel-time"></span>' + schedule.goingDuration + 'min.';
    },
    comingDuration: function(schedule) {
        return '<span class="calendar-icon ic-travel-time"></span>' + schedule.comingDuration + 'min.';
    },
    monthMoreTitleDate: function(date, dayname) {
        var day = date.split('.')[2];
        return '<span class="tui-full-calendar-month-more-title-day">' + day + '</span> <span class="tui-full-calendar-month-more-title-day-label">' + dayname + '</span>';
    },
    monthMoreClose: function() {
        return '<span class="tui-full-calendar-icon tui-full-calendar-ic-close"></span>';
    },
    monthGridHeader: function(dayModel) {
        var date = parseInt(dayModel.date.split('-')[2], 10);
        var classNames = ['tui-full-calendar-weekday-grid-date '];

        if (dayModel.isToday) {
            classNames.push('tui-full-calendar-weekday-grid-date-decorator');
        }

        return '<span class="' + classNames.join(' ') + '">' + date + '</span>';
    },
    monthGridHeaderExceed: function(hiddenSchedules) {
        return '<span class="weekday-grid-more-schedules">+' + hiddenSchedules + '</span>';
    },
    monthGridFooter: function() {
        return '';
    },
    monthGridFooterExceed: function(hiddenSchedules) {
        return '';
    },
    monthDayname: function(model) {
        return String(model.label).toLocaleUpperCase();
    },
    dayGridTitle: function(viewName) {
        /*
         * use another functions instead of 'dayGridTitle'
         * milestoneTitle: function() {...}
         * taskTitle: function() {...}
         * alldayTitle: function() {...}
        */

        return getGridTitleTemplate(viewName);
    },
    schedule: function(schedule) {
        /*
         * use another functions instead of 'schedule'
         * milestone: function() {...}
         * task: function() {...}
         * allday: function() {...}
        */

        return getGridCategoryTemplate(schedule.category, schedule);
    },
    popupIsAllDay: function() {
        return 'All Day';
    },
    popupStateFree: function() {
        return 'Free';
    },
    popupStateBusy: function() {
        return 'Busy';
    },
    titlePlaceholder: function() {
        return 'Subject';
    },
    locationPlaceholder: function() {
        return 'Location';
    },
    startDatePlaceholder: function() {
        return 'Start date';
    },
    endDatePlaceholder: function() {
        return 'End date';
    },
    popupSave: function() {
        return 'Save';
    },
    popupUpdate: function() {
        return 'Update';
    },
    popupDetailDate: function(isAllDay, start, end) {
        var isSameDate = moment(start).isSame(end);
        var endFormat = (isSameDate ? '' : 'YYYY.MM.DD ') + 'hh:mm a';

        if (isAllDay) {
            return moment(start).format('YYYY.MM.DD') + (isSameDate ? '' : ' - ' + moment(end).format('YYYY.MM.DD'));
        }

        return (moment(start).format('YYYY.MM.DD hh:mm a') + ' - ' + moment(end).format(endFormat));
    },
    popupDetailLocation: function(schedule) {
        return 'Location : ' + schedule.location;
    },
    popupDetailUser: function(schedule) {
        return 'User : ' + (schedule.attendees || []).join(', ');
    },
    popupDetailState: function(schedule) {
        return 'State : ' + schedule.state || 'Busy';
    },
    popupDetailRepeat: function(schedule) {
        return 'Repeat : ' + schedule.recurrenceRule;
    },
    popupDetailBody: function(schedule) {
        return 'Body : ' + schedule.body;
    },
    popupEdit: function() {
        return 'Edit';
    },
    popupDelete: function() {
        return 'Delete';
    }
};

var cal = new tui.Calendar('#calendar', {
    defaultView: 'month',
    template: templates
});

// var calendar = {
//     init: function () {
//         var cal = new tui.Calendar('#calendar', {
//             defaultView: 'month',
//             template: templates,
//             useCreationPopup: true,
//             useDetailPopup: true
//         });
//
//         console.log(cal);
//     }
// }