@import dev.learnlab.exercise.TimetableView
@param TimetableView timetable
<section id="grid">
    <table>
        <caption>Orario delle classi</caption>
        <thead>
        <tr>
            <th scope="col">Ora</th>
            @for(String schoolClass : timetable.classes())
                <th scope="col">${schoolClass}</th>
            @endfor
        </tr>
        </thead>
        <tbody>
        @for(TimetableView.Row row : timetable.rows())
            <tr>
                <th scope="row">${row.slot()}</th>
                @for(String cell : row.cells())
                    <td>${cell}</td>
                @endfor
            </tr>
        @endfor
        </tbody>
    </table>
    <p role="status">@if(timetable.conflicts() == 0)Nessun conflitto.@else${timetable.conflicts()} conflitti.@endif</p>
</section>
