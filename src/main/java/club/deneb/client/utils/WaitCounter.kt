package club.deneb.client.utils

class WaitCounter {
    private var waitTick = 0

    fun passed(ticks:Int):Boolean{
        return waitTick >= ticks
    }

    fun reset(){
        waitTick = 0
    }

    fun addTick(){
        waitTick++
    }

    fun addTick(ticks: Int){
        waitTick+= ticks
    }
}