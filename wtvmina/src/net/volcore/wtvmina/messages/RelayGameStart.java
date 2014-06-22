/** Copyright (C) 2008 Volker Schönefeld. See the copyright notice in the LICENSE file. */
package net.volcore.wtvmina.messages;

//imports
    // slf4j
        import org.slf4j.*;
    // volcore libs
        import net.volcore.util.*;
        import net.volcore.wtvmina.*;
    // mina
        import org.apache.mina.core.buffer.*;
    // java
        import java.util.*;
        import java.nio.*;
        import java.nio.charset.*;

/*******************************************************************************
         See wtvProtocol.h in docs.
 *******************************************************************************/
public class RelayGameStart implements WTVMessage
{
    static Logger   logger = LoggerFactory.getLogger( "RelayGameStart" );

    public int     gameid;
    public int     lastSeed;
    public int     delay;
    public int     date;

    protected final CharsetDecoder charsetDecoder = Charset.forName( "UTF-8" ).newDecoder( ).onMalformedInput( CodingErrorAction.REPLACE ).onUnmappableCharacter( CodingErrorAction.REPLACE );
    protected final CharsetEncoder charsetEncoder = Charset.forName( "UTF-8" ).newEncoder( ).onMalformedInput( CodingErrorAction.REPLACE ).onUnmappableCharacter( CodingErrorAction.REPLACE );

    public RelayGameStart( int gameid, int lastseed, int delay, int date )
    {
        this.gameid = gameid;
        this.lastSeed = lastseed;
        this.delay = delay;
        this.date = date;
    }

    public RelayGameStart( IoBuffer in ) throws ParseException
    {
        gameid = in.getInt( );
        lastSeed = in.getInt( );
        delay = in.getInt( );
        date = in.getInt( );
    }

    public IoBuffer assemble( )
    {
        try {
            short length = (short)(5+16);

            IoBuffer buf = IoBuffer.allocate( length );
            buf.order( ByteOrder.LITTLE_ENDIAN );
            buf.putShort( WTVProtocolDecoder.PRIMER );
            buf.put( WTVMessageFactory.RELAY_GAMESTART );
            buf.putShort( length );
            buf.putInt( gameid );
            buf.putInt( lastSeed );
            buf.putInt( delay );
            buf.putInt( date );
            buf.flip( );
            return buf;
        } catch( Exception e )
        {
            logger.error( "Failed to assemble "+this.getClass( )+": "+e );
            e.printStackTrace( );
            return null;
        }
    }

    public String toString( )
    {
        return "("+this.getClass( ).getName( )+": "+gameid+" "+lastSeed+" "+delay+" "+date+")";
    }
}


