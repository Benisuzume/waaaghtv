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
public class RelaySubscribeGameReply implements WTVMessage
{
    static Logger   logger = LoggerFactory.getLogger( "RelaySubscribeGameReply" );

    public int     gameid;
    public byte    status; // 1 downloading, 0 failure

    protected final CharsetDecoder charsetDecoder = Charset.forName( "UTF-8" ).newDecoder( ).onMalformedInput( CodingErrorAction.REPLACE ).onUnmappableCharacter( CodingErrorAction.REPLACE );
    protected final CharsetEncoder charsetEncoder = Charset.forName( "UTF-8" ).newEncoder( ).onMalformedInput( CodingErrorAction.REPLACE ).onUnmappableCharacter( CodingErrorAction.REPLACE );

    public RelaySubscribeGameReply( int gameid, byte status ) throws ParseException
    {
        this.gameid = gameid;
        this.status = status;
    }

    public RelaySubscribeGameReply( IoBuffer in ) throws ParseException
    {
        gameid = in.getInt( );
        status = in.get( );
    }

    public IoBuffer assemble( )
    {
        try {
            short length = (short)(5+5);

            IoBuffer buf = IoBuffer.allocate( length );
            buf.order( ByteOrder.LITTLE_ENDIAN );
            buf.putShort( WTVProtocolDecoder.PRIMER );
            buf.put( WTVMessageFactory.RELAY_SUBSCRIBEGAMEREPLY );
            buf.putShort( length );
            buf.putInt( gameid );
            buf.put( status );
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
        return "("+this.getClass( ).getName( )+": "+gameid+" "+status+")";
    }
}

