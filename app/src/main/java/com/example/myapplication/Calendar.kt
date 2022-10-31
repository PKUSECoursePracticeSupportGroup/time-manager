package com.example.myapplication

import android.graphics.Paint.Align
import android.widget.DatePicker
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.myapplication.backstage.CourseTemplate
import com.example.myapplication.backstage.Schedule
import org.intellij.lang.annotations.JdkConstants.TitledBorderTitlePosition

val weekday = arrayListOf<String>("Mon","Tue","Wed","Thu","Fri","Sat","Sun")


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(week:Int = 0)
{
    Scaffold(
        topBar = {TopBar(week)},
    ){
       CalendarGrid(week)
    }
}

@Preview
@Composable
fun previewweekselector()
{
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        CenterText(text = "Week 0", fontsize = 15.sp)
        WeekSelector()
    }
}
@Composable
fun TopBar(week:Int)
{
    SmallTopAppBar(
        title = {
                //WeekSelector()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Week $week")
                WeekSelector()
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Menu, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        })
}
@Composable
fun WeekSelector()
{
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { /* Handle edit! */ },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                })
        }
    }
}
@Composable
fun CalendarGrid(weekIndex:Int)
{
    LazyRow(
        modifier = Modifier.padding(10.dp,0.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        item {
            Column() {
                val width = 100.dp
                Row() {
                    for (i in 0..6) {
                        CenterText(modifier = Modifier.padding(5.dp,0.dp).width(width), text = weekday[i])
                    }
                }
                LazyColumn(
                    modifier = Modifier.height(760.dp)
                ) {
                    item{ Spacer(modifier = Modifier.padding(10.dp))}
                    item {
                        Row()
                        {

                            for (i in 0..6) {
                                DailyList(Modifier.padding(5.dp,0.dp),weekIndex, i,width = width)
                            }
                        }
                    }
                    item{
                        Spacer(modifier = Modifier.padding(50.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TimeList()
{
    Column(modifier = Modifier.width(20.dp)){
        Text(text = "")
        Spacer(modifier = Modifier.padding(10.dp))
        for(i in 0..11)
        {

        }
    }
}

@Composable
fun DailyList(modifier:Modifier = Modifier,weekIndex: Int,dayIndex: Int,width:Dp = 100.dp)
{
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {

        // get class info
        var i:Short = 0
        var activity = LocalContext.current as MainActivity
        var schedule = activity.schedule

        //render classBlock
        while(i<12)
        {
            var course:CourseTemplate?= schedule.getTemplate(i,dayIndex.toShort(),weekIndex.toShort())
            var len:Int = 1
            if(course == null)
            {
                len = 1
                ClassBlock({ Text(text = "") }, MaterialTheme.colorScheme.background,len, width)
            }
            else {
                var coursename = course.info.Name
                var courselocation = course.info.Location
                len = course?.EndingTime!! - course?.StartingTime!!
                ClassBlock({
                    LazyColumn() {
                        item {
                            CenterText(modifier = Modifier.width(width),text = coursename)
                            CenterText(modifier = Modifier.width(width),text = courselocation)
                        }
                    }
                }, MaterialTheme.colorScheme.secondary, len,width)
            }

            i = (i + len).toShort()
        }
    }
}

@Composable
fun CenterText(modifier: Modifier = Modifier,text:String, fontsize: TextUnit = 13.sp)
{
    Text(
        text = text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontSize = fontsize
    )
}

@Composable
fun ClassBlock(content:@Composable ()->Unit,color:Color,len:Int,width:Dp)
{
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(width)
            .height(len * 60.dp + (len - 1) * 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(10.dp),
        elevation = ButtonDefaults.buttonElevation(2.dp,1.dp,2.dp)

    ) {
        content()
    }
}
@Preview
@Composable
fun previewclassblock()
{
    ClassBlock(content = {Text(text = "114514")},color = Color.LightGray, len = 1, width = 100.dp)
}

