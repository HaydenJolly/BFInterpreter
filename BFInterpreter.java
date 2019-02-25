public class BFInterpreter
{
    private String instructions;
    private char instruction;
    private byte [] mem;
    private int memIndex, instructionIndex, instructionEnd;

    public BFInterpreter(String _instructions)
    {
        instructions = _instructions;
        mem = new byte [30000];
        memIndex = 0;
        instructionIndex = -1;
        instructionEnd = instructions.length();
    }

    public void runProgram()
    {
        //iterate over all instructions
        while(++instructionIndex < instructionEnd)
        {
            switch (instructions.charAt(instructionIndex)){
                case '+': //increments memory at memIndex (wraps around to 0 after 256)
                mem[memIndex] = (byte)(++mem[memIndex] % 256);
                break;

                case '-': //decrement memory at memIndex (wraps to 255 below 0)
                  mem[memIndex] = (mem[memIndex] > 0) ? mem[memIndex] - 1 : (byte)255;
                  break;

                case '>': //points memIndex one unit forward (wraps back to 0)
                  memIndex = ++memIndex % 30000;
                  break;

                case '<': //points memIndex one unit backwards (wraps to end)
                  memIndex = (memIndex > 0) ? memIndex - 1 : 29999;
                  break;

                case '[': //checks loop conidition and seeks loop close
                  if (mem[memIndex] == 0) instructionIndex = findLoopClose();
                  break;

                case ']': //seeks loop open
                  instructionIndex = findLoopOpen();
                  break;

                case '.': //outputs the ascii character associated with current memory value
                  System.out.print((char)mem[memIndex]);
                  break;

                case ',': //sets the current memory location to the value of an ascii character input
                  System.out.print("\nEnter an ASCII character whose value will be stored in the current cell: ");
                  mem[memIndex] = (byte)in.next().charAt(0);
                  in.nextLine();
                  break;
            }
        }
    }

    private int findLoopClose()
    {
        int loopOpens = 0;
        boolean foundEnd = false;
        char localInstruction;
        int localInstructionIndex = instructionIndex;
        while (!foundEnd)
        {
            localInstruction = instructions.charAt(localInstructionIndex);
            if (localInstruction == '[') loopOpens++;
            else if (localInstruction == ']') loopOpens--;

            foundEnd = loopOpens == 0 && localInstruction == ']';
            localInstructionIndex++;
        }
        return --localInstructionIndex;
    }

    private int findLoopOpen()
    {
        int loopEnds = 0;
        boolean foundOpen = false;
        char localInstruction;
        int localInstructionIndex = instructionIndex;
        while (!foundOpen)
        {
            localInstruction = instructions.charAt(localInstructionIndex);
            if (localInstruction == ']') loopEnds++;
            else if (localInstruction == '[') loopEnds--;

            foundOpen = loopEnds == 0 && localInstruction == '[';
            localInstructionIndex--;
        }
        return localInstructionIndex;
    }
}
